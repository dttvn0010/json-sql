package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;

public class JSonSQL {

    public static List<Map<String, Object>> query(List<Map<String, Object>> table, String sql) throws JSQLParserException {
        Select stmt = (Select) CCJSqlParserUtil.parse(sql);
        //TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        //List<String> tableList = tablesNamesFinder.getTableList(stmt);
        
        PlainSelect plainSelect = (PlainSelect)stmt.getSelectBody();
        Expression where = plainSelect.getWhere();
        
        List<SelectExpressionItem> selectItems = new ArrayList<>();       
        for (SelectItem selectItem : plainSelect.getSelectItems()) {
            selectItem.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    selectItems.add(item);
                }
            });
        }
        
        List<Map<String, Object>> resultSet = new ArrayList<>();
        
        for(Map<String, Object> row: table) {
            boolean selected = where != null? (Boolean) Eval.evaluate(where, row) : true;
            if(selected) {
                Map<String, Object> result = new HashMap<>();
                for(SelectExpressionItem item : selectItems ) {
                    String colName = item.getAlias() != null? item.getAlias().getName(): item.toString();
                    result.put(colName, Eval.evaluate(item.getExpression(), row));
                }
                resultSet.add(result);
            }
        }
        return resultSet;
    }
}
