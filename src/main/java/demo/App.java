package demo;

import net.sf.jsqlparser.JSQLParserException;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App 
{
    
    public static void main( String[] args ) throws JSQLParserException, Exception
    {
        String json = "[{\"col1\": 2, \"col2\": 3 }, {\"col1\": 5, \"col2\": 17 }]";
        String sql = "select col1+1 as a, col2 as b from table where col1>2 AND (col2 > 10 OR col2 < 1)";
        
        System.out.println("Json:" + json);
        System.out.println("sql:" + sql);
        
        ObjectMapper mapper = new ObjectMapper();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> table = mapper.readValue(json, List.class);
        
        List<Map<String, Object>> resultSet = JSonSQL.query(table, sql);
        
        String jsonOutput = mapper.writeValueAsString(resultSet);
        System.out.println("Query result:" + jsonOutput);
    }
}
