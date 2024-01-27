package fi.ollipuljula.tuplaus;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
public class TuplausConfiguration {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("tuplaus_db")
                .setType(HSQL)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("data/schema.sql")
                .addScripts("data/data.sql")
                .build();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server hsqlServer() throws ServerAcl.AclFormatException, IOException {
        Server server = new Server();
        server.setProperties(new HsqlProperties(new Properties() {{
            put("server.database.0", "mem:tuplaus_db;user=sa");
            put("server.dbname.0", "tuplaus_db");
            put("server.port", "12345");
        }}));
        return server;
    }
}
