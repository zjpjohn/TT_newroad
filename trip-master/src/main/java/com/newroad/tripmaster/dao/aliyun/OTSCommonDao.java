package com.newroad.tripmaster.dao.aliyun;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ots.OTSClient;
import com.aliyun.openservices.ots.model.CapacityUnit;
import com.aliyun.openservices.ots.model.ColumnValue;
import com.aliyun.openservices.ots.model.Condition;
import com.aliyun.openservices.ots.model.CreateTableRequest;
import com.aliyun.openservices.ots.model.DeleteRowRequest;
import com.aliyun.openservices.ots.model.DeleteRowResult;
import com.aliyun.openservices.ots.model.DeleteTableRequest;
import com.aliyun.openservices.ots.model.GetRowRequest;
import com.aliyun.openservices.ots.model.GetRowResult;
import com.aliyun.openservices.ots.model.ListTableResult;
import com.aliyun.openservices.ots.model.PrimaryKeyType;
import com.aliyun.openservices.ots.model.PrimaryKeyValue;
import com.aliyun.openservices.ots.model.PutRowRequest;
import com.aliyun.openservices.ots.model.PutRowResult;
import com.aliyun.openservices.ots.model.Row;
import com.aliyun.openservices.ots.model.RowDeleteChange;
import com.aliyun.openservices.ots.model.RowExistenceExpectation;
import com.aliyun.openservices.ots.model.RowPrimaryKey;
import com.aliyun.openservices.ots.model.RowPutChange;
import com.aliyun.openservices.ots.model.RowUpdateChange;
import com.aliyun.openservices.ots.model.SingleRowQueryCriteria;
import com.aliyun.openservices.ots.model.TableMeta;
import com.aliyun.openservices.ots.model.UpdateRowRequest;
import com.aliyun.openservices.ots.model.UpdateRowResult;
import com.newroad.tripmaster.dao.pojo.Site;

public class OTSCommonDao {

  private static final Logger log = LoggerFactory.getLogger(OTSCommonDao.class);

  public OTSClient otsClient;

  public OTSCommonDao(OTSClient otsClient) {
    super();
    this.otsClient = otsClient;
  }

  public boolean createTable(String tableName) {
    String COLUMN_GID_NAME = "gid";
    String COLUMN_UID_NAME = "uid";

    TableMeta tableMeta = new TableMeta(tableName);
    tableMeta.addPrimaryKeyColumn(COLUMN_GID_NAME, PrimaryKeyType.INTEGER);
    tableMeta.addPrimaryKeyColumn(COLUMN_UID_NAME, PrimaryKeyType.INTEGER);
    // 将该表的读写CU都设置为100
    CapacityUnit capacityUnit = new CapacityUnit(100, 100);

    CreateTableRequest request = new CreateTableRequest();
    request.setTableMeta(tableMeta);
    request.setReservedThroughput(capacityUnit);
    otsClient.createTable(request);

    System.out.println("Table " + tableName + " has been created!");
    log.info("Table " + tableName + " has been created!");
    return true;
  }

  public boolean deleteTable(String tableName) {
    DeleteTableRequest request = new DeleteTableRequest();
    request.setTableName(tableName);
    otsClient.deleteTable(request);
    System.out.println("Table " + tableName + " has been deleted!");
    log.info("Table " + tableName + " has been deleted!");
    return true;
  }

  public List<String> listTable() {
    List<String> tableNameList = new ArrayList<String>();
    ListTableResult result = otsClient.listTable();

    for (String tableName : result.getTableNames()) {
      tableNameList.add(tableName);
      System.out.println("Table Name " + tableName + " listed!");
      log.info("Table Name " + tableName + " listed!");
    }
    return tableNameList;
  }
  
  public boolean putRow(String tableName,Site location){
    String COLUMN_GID_NAME = "gid";
    String COLUMN_UID_NAME = "uid";
    String COLUMN_NAME_NAME = "name";
    String COLUMN_X_NAME = "coordinateX";
    String COLUMN_Y_NAME = "coordinateY";
    String COLUMN_AREA_NAME = "area";
     
    RowPutChange rowChange = new RowPutChange(tableName);
    RowPrimaryKey primaryKey = new RowPrimaryKey();
    primaryKey.addPrimaryKeyColumn(COLUMN_GID_NAME, PrimaryKeyValue.fromLong(1));
    primaryKey.addPrimaryKeyColumn(COLUMN_UID_NAME, PrimaryKeyValue.fromLong(101));
    rowChange.setPrimaryKey(primaryKey);
    rowChange.addAttributeColumn(COLUMN_NAME_NAME, ColumnValue.fromString("testName2"));
    rowChange.addAttributeColumn(COLUMN_X_NAME, ColumnValue.fromString("114.30769"));
    rowChange.addAttributeColumn(COLUMN_Y_NAME, ColumnValue.fromString("42.05838"));
    rowChange.addAttributeColumn(COLUMN_AREA_NAME, ColumnValue.fromString("Shanghai"));
    rowChange.setCondition(new Condition(RowExistenceExpectation.EXPECT_NOT_EXIST)); 
     
    PutRowRequest request = new PutRowRequest();
    request.setRowChange(rowChange);
     
    PutRowResult result = otsClient.putRow(request);
    int consumedWriteCU = result.getConsumedCapacity().getCapacityUnit().getWriteCapacityUnit();
     
    log.info("Table " + tableName + " has been inserted one record and use CapacityUnit is "+consumedWriteCU+"!");
    return true;
  }
  
  public boolean getRow(String tableName){
    String COLUMN_GID_NAME = "gid";
    String COLUMN_UID_NAME = "uid";
    String COLUMN_NAME_NAME = "name";
    String COLUMN_X_NAME = "coordinateX";
    String COLUMN_Y_NAME = "coordinateY";
    String COLUMN_AREA_NAME = "area";
     
    SingleRowQueryCriteria criteria = new SingleRowQueryCriteria(tableName);
    RowPrimaryKey primaryKeys = new RowPrimaryKey();
    primaryKeys.addPrimaryKeyColumn(COLUMN_GID_NAME, PrimaryKeyValue.fromLong(1));
    primaryKeys.addPrimaryKeyColumn(COLUMN_UID_NAME, PrimaryKeyValue.fromLong(101));
    criteria.setPrimaryKey(primaryKeys);
    criteria.addColumnsToGet(new String[] {
            COLUMN_NAME_NAME,
            COLUMN_X_NAME,
            COLUMN_Y_NAME,
            COLUMN_AREA_NAME
    });
     
    GetRowRequest request = new GetRowRequest();
    request.setRowQueryCriteria(criteria);
    GetRowResult result = otsClient.getRow(request);
    Row row = result.getRow();
     
    int consumedReadCU = result.getConsumedCapacity().getCapacityUnit().getReadCapacityUnit();
    log.info("Get Table " + tableName + " info operation uses CapacityUnit is "+consumedReadCU+"!");
    log.info("Name info:" + row.getColumns().get(COLUMN_NAME_NAME));
    log.info("X info:" + row.getColumns().get(COLUMN_X_NAME));
    log.info("Y info:" + row.getColumns().get(COLUMN_Y_NAME));
    log.info("Aear info:" + row.getColumns().get(COLUMN_AREA_NAME));
    
    return true;
  }
  
  public boolean updateRow(String tableName,Site location){
    String COLUMN_GID_NAME = "gid";
    String COLUMN_UID_NAME = "uid";
    String COLUMN_NAME_NAME = "name";
    String COLUMN_X_NAME = "coordinateX";
    String COLUMN_Y_NAME = "coordinateY";
    String COLUMN_AREA_NAME = "area";
     
    RowUpdateChange rowChange = new RowUpdateChange(tableName);
    RowPrimaryKey primaryKeys = new RowPrimaryKey();
    primaryKeys.addPrimaryKeyColumn(COLUMN_GID_NAME, PrimaryKeyValue.fromLong(1));
    primaryKeys.addPrimaryKeyColumn(COLUMN_UID_NAME, PrimaryKeyValue.fromLong(101));
    rowChange.setPrimaryKey(primaryKeys);
    // 更新以下三列的值
    rowChange.addAttributeColumn(COLUMN_NAME_NAME, ColumnValue.fromString("testName2"));
    rowChange.addAttributeColumn(COLUMN_AREA_NAME, ColumnValue.fromString("Beijing"));

//    rowChange.deleteAttributeColumn(COLUMN_MOBILE_NAME);
//    rowChange.deleteAttributeColumn(COLUMN_AGE_NAME);
     
    rowChange.setCondition(new Condition(RowExistenceExpectation.EXPECT_EXIST));
     
    UpdateRowRequest request = new UpdateRowRequest();
    request.setRowChange(rowChange);
     
    UpdateRowResult result = otsClient.updateRow(request);
    int consumedWriteCU = result.getConsumedCapacity().getCapacityUnit().getWriteCapacityUnit();
     
    log.info("Table " + tableName + " has been updated one record and use CapacityUnit is "+consumedWriteCU+"!");
    return true;
  }
  
  public boolean deleteRow(String tableName){
    String COLUMN_GID_NAME = "gid";
    String COLUMN_UID_NAME = "uid";
     
    RowDeleteChange rowChange = new RowDeleteChange(tableName);
    RowPrimaryKey primaryKeys = new RowPrimaryKey();
    primaryKeys.addPrimaryKeyColumn(COLUMN_GID_NAME, PrimaryKeyValue.fromLong(1));
    primaryKeys.addPrimaryKeyColumn(COLUMN_UID_NAME, PrimaryKeyValue.fromLong(101));
    rowChange.setPrimaryKey(primaryKeys);
     
    DeleteRowRequest request = new DeleteRowRequest();
    request.setRowChange(rowChange);
     
    DeleteRowResult result = otsClient.deleteRow(request);
    int consumedWriteCU = result.getConsumedCapacity().getCapacityUnit().getWriteCapacityUnit();
     
    log.info("Table " + tableName + " has been deleted one record and use CapacityUnit is "+consumedWriteCU+"!");
    return true;
  }
  

}
