功能描述:
    1.用于系统部署时 数据库表的初始化。
    2.可用于表结构的创建、删除、覆盖(原来的表删除 使用新的表结构) 

使用说明:
    1.建表语句使用xml格式归档，文件名必须为tables.xml
    2.根据需求编写表结构后 请覆盖table-tools-0.0.1-SNAPSHOT-jar-with-dependencies.jar包下的tables.xml文件
    3.具体格式可参考  table-tools-0.0.1-SNAPSHOT-jar-with-dependencies.jar包下的tables.xml文件

    注：xml结构说明 
	<table></table>  表示一个表空间
		name: 表名
		operate: 操作 (create, replace, delete)
	    <column></column> 一条数据库列
			name:       列名
			type:       类型 (int,string,boolean,array,long,document,float)
			required:   是否可为空 (true/false)
			des:        列描述
			def:        默认值 (暂时csd不支持)
	    <index></index> 一条数据库索引
			column:     列名
	        name:       索引名
			unique:     是否唯一 (true/false)
		<init-data></init-data> 表空间初始化数据
			<data></data> 一条初始化数据
				<value></value> 一列数据的值
					name:      列名
					value:     值
			注： 初始化数据时,若有使用表默认值的列可不填, 系统根据列定义的默认值补全.
运行:
    1.将该支持包发布到应用环境 (通过 maven assembly 插件打包后 在项目 target下面生成两个包 直接使用这个table-tools-0.0.1-SNAPSHOT-jar-with-dependencies.jar 包含了所需要的jar依赖 )
    2.执行java -jar table-tools-0.0.1-SNAPSHOT-jar-with-dependencies.jar