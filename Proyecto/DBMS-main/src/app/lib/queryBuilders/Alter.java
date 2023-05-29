package app.lib.queryBuilders;

public class Alter implements QueryBuilder {
  private final String template = "ALTER TABLE %s %s;";
  private String tableName;
  private AlterAction action;
  private final static Alter _instance = new Alter();

  public static Alter add(String tableName, ColumnEntry columnEntry) {
    return new Alter(tableName, _instance.new AlterAction(Action.ADD, columnEntry));
  }

  public static Alter drop(String tableName, String columnName) {
    return new Alter(tableName, _instance.new AlterAction(Action.DROP, columnName));
  }

  public static Alter rename(String tableName, String columnName, String newColumnName) {
    return new Alter(tableName, _instance.new AlterAction(Action.RENAME, columnName, newColumnName));
  }

  public static Alter alter(String tableName, ColumnEntry columnEntry) {
    return new Alter(tableName, _instance.new AlterAction(Action.ALTER, columnEntry));
  }

  private Alter() {}
  
  private Alter(String tableName, AlterAction action) {
    this.tableName = tableName;
    this.action = action;
  }

  @Override
  public String generateQuery(Object... params) {
    return String.format(this.template,this.tableName,this.action.toString());
  }


  private class AlterAction {
    private String columnName;
    private String newColumnName;
    private ColumnEntry column;
    private Action action;
  
    public AlterAction(Action action, String columnName) {
      this.action = action;
      this.columnName = columnName;
    }
  
    public AlterAction(Action action, String columnName, String newColumnName) {
      this.action = action;
      this.columnName = columnName;
      this.newColumnName = newColumnName;
    }
  
    public AlterAction(Action action, ColumnEntry columnEntry) {
      this.action = action;
      this.column = columnEntry;
    }
  
    @Override
    public String toString() {
      if (this.action.equals(Action.DROP)) {
        return String.format(this.action.toString(),this.columnName);
      }
  
      if (this.action.equals(Action.RENAME)) {
        return String.format(this.action.toString(),this.columnName, this.newColumnName);
      }
  
      return String.format(this.action.toString(), this.column.toString());
    }
  
  }

  private enum Action {
    ADD("ADD %s"), ALTER("ALTER COLUMN %s"), DROP("DROP %s"), RENAME("RENAME %s TO %s");
    
    private String syntax;
    private Action(String syntax) {
      this.syntax = syntax;
    }
  
    @Override
    public String toString() {
      return this.syntax;
    }
  }
}
