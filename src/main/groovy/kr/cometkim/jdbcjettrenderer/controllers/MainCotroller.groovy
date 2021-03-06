package kr.cometkim.jdbcjettrenderer.controllers

import net.sf.jett.jdbc.JDBCExecutor
import net.sf.jett.transform.ExcelTransformer
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

import java.sql.DatabaseMetaData
import java.sql.DriverManager

@Controller
class MainCotroller {
    @GetMapping('/')
    String index() { 'index' }

    @PostMapping('/test')
    @ResponseBody
    String testConnection(
        @RequestParam('host') String host,
        @RequestParam('port') Integer port,
        @RequestParam('database') String dbname,
        @RequestParam('username') String username,
        @RequestParam('password') String password
    ) {
        try {
            def conn = DriverManager.getConnection(
                "jdbc:mysql://${host}:${port}/${dbname}",
                username,
                password,
            )
            conn.close()
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
            return "Connection failed: ${e.message}"
        }
        return "Connected successfully"
    }

    @PostMapping('/render')
    ResponseEntity<ByteArrayResource> renderTemplate(
        @RequestParam('host') String host,
        @RequestParam('port') Integer port,
        @RequestParam('database') String dbname,
        @RequestParam('username') String username,
        @RequestParam('password') String password,
        @RequestParam('template') MultipartFile template
    ) {
        def conn = null
        def fileOut = null
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://${host}:${port}/${dbname}",
                username,
                password,
            )
            def jdbc = new JDBCExecutor(conn)
            def metadata = conn.metaData
            def tables = getTableInfoList(metadata)

            def ctx = [
                jdbc  : jdbc,
                tables: tables,
            ]

            def fileIn = template.inputStream
            fileOut = new ByteArrayOutputStream()

            new ExcelTransformer()
                .transform(fileIn, ctx)
                .write(fileOut)

            ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${template.originalFilename}\"")
                .body(new ByteArrayResource(fileOut.toByteArray()))
        } catch (Exception e) {
            e.printStackTrace()
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body(e.message)
        } finally {
            conn?.close()
            fileOut?.close()
        }
    }

    private List<Object> getTableInfoList(DatabaseMetaData metadata) {
        def tableInfoList = []

        def tableMetadata = metadata.getTables(null, null, null, ['TABLE'] as String[])
        while(tableMetadata.next()) {
            def tableName = tableMetadata.getString('TABLE_NAME')
            def tableInfo = [
                name: tableName,
                comment: tableMetadata.getString('REMARKS'),
                columns: getColumnInfoList(metadata, tableName),
            ]
            tableInfoList += tableInfo
        }

        return tableInfoList
    }

    private List<Object> getColumnInfoList(DatabaseMetaData metadata, String tableName) {
        def columnInfoList = []

        def columnMetadata = metadata.getColumns(null, null, tableName, null)
        while (columnMetadata.next()) {
            def columnInfo = [
                name: columnMetadata.getString('COLUMN_NAME'),
                type: columnMetadata.getString('TYPE_NAME'),
                comment: columnMetadata.getString('REMARKS'),
                isNullable: columnMetadata.getString('IS_NULLABLE') == 'YES',
                isAutoIncrement: columnMetadata.getString('IS_AUTOINCREMENT') == 'YES',
            ]
            columnInfoList += columnInfo
        }

        return columnInfoList
    }
}

