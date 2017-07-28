<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN" "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
#*该文件在MybatisGeneratorUtil中被VelocityUtil.generate(...)方法装配 ! )*#
<!-- Mybatis Generator（简称MBG）配置生成器（Mybatis 使用mybatis_Generator 生成PO类 、mapper.java 类、Mappering.xml映射文件，以及各模块下的generatorConfig.xml文件）:
MyBatis Generator生成器自动生成代码：详解：http://www.jianshu.com/p/e09d2370b796?utm_campaign=maleskine&utm_content=note&utm_medium=pc_all_hots&utm_source=recommendation -->
<generatorConfiguration>
    <!-- 可以用于加载配置项或者配置文件，在整个配置文件中就可以使用${ propertyKey }的方式来引用配置项
    resource：配置资源加载地址，使用resource，MBG从classpath开始找，比如com/myproject/generatorConfig.properties
    url：配置资源加载地质，使用URL的方式，比如file:///C:/myfolder/generatorConfig.properties.         注意，两个属性只能选址一个;
    另外，如果使用了mybatis-generator-maven-plugin，那么在pom.xml中定义的properties都可以直接在generatorConfig.xml中使用<properties resource="" url="" />
    -->

    <!-- 在MBG工作的时候，需要额外加载的依赖包
        location属性指明加载jar/zip包的全路径
   <classPathEntry location="/Program Files/IBM/SQLLIB/java/db2java.zip" />
     -->

    <!--
        context:生成一组对象的环境
        id:必选，上下文id，用于在生成错误时提示
        defaultModelType:指定生成对象的样式
            1，conditional：类似hierarchical；
            2，flat：所有内容（主键，blob）等全部生成在一个对象中；
            3，hierarchical：主键生成一个XXKey对象(key class)，Blob等单独生成一个对象，其他简单属性在一个对象中(record class)
        targetRuntime:
            1，MyBatis3：默认的值，生成基于MyBatis3.x以上版本的内容，包括XXXBySample.java；
            2，MyBatis3Simple：类似MyBatis3，只是不生成XXXBySample.java；
        introspectedColumnImpl：类全限定名，用于扩展MBG
    -->

    <!-- mysql配置文件 切面-->
    <properties resource="generator.properties"></properties>

    <context id="MysqlContext" targetRuntime="MyBatis3" defaultModelType="flat">

        <property name="javaFileEncoding" value="UTF-8"/> <!-- 生成的Java文件的编码 -->
        <!-- 由于beginningDelimiter和endingDelimiter的默认值为双引号(")，ORACLE就是双引号,在Mysql中不能这么写，所以还要将这两个默认值改为`  -->
        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <!-- 为生成的Java模型创建一个toString方法 -->
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin"></plugin>

        <!-- 为生成的Java模型类添加序列化接口，并生成serialVersionUID字段 -->
        <plugin type="com.zheng.common.plugin.SerializablePlugin">
            <property name="suppressJavaInterface" value="false"/>
        </plugin>

        <!-- 生成一个新的selectByExample方法，这个方法可以接收offset和limit参数，主要用来实现分页，只支持mysql(已使用pagehelper代替) -->
        <!--<plugin type="com.zheng.common.plugin.PaginationPlugin"></plugin>-->

        <!-- 生成在XML中的<cache>元素 -->
        <plugin type="org.mybatis.generator.plugins.CachePlugin">
            <!-- 使用ehcache -->
            <property name="cache_type" value="org.mybatis.caches.ehcache.LoggingEhcache" />
            <!-- 内置cache配置 -->
            <!--
            <property name="cache_eviction" value="LRU" />
            <property name="cache_flushInterval" value="60000" />
            <property name="cache_readOnly" value="true" />
            <property name="cache_size" value="1024" />
            -->
        </plugin>

        <!-- Java模型生成equals和hashcode方法 -->
        <plugin type="org.mybatis.generator.plugins.EqualsHashCodePlugin"></plugin>

        <!-- 生成的代码去掉注释 -->
        <commentGenerator type="com.zheng.common.plugin.CommentGenerator">
            <property name="suppressAllComments" value="true" />
            <property name="suppressDate" value="true"/>
        </commentGenerator>

        <!-- 数据库连接 -->
        <jdbcConnection driverClass="${generator.jdbc.driver}"
                        connectionURL="${generator.jdbc.url}"
                        userId="${generator.jdbc.username}"
                        password="${generator_jdbc_password}" />

        #*所有的变量都在 MybatisGeneratorUtil.java 中*#
        <!-- model生成
        java模型创建器，是必须要的元素
        负责：1，key类（见context的defaultModelType）；2，java类；3，查询类
        targetPackage：生成的类要放的包，真实的包受enableSubPackages属性控制；
        targetProject：目标项目，指定一个存在的目录下，生成的内容会放到指定目录中，如果目录不存在，MBG不会自动建目录
         -->
        <javaModelGenerator targetPackage="${generator_javaModelGenerator_targetPackage}" targetProject="${targetProject}/src/main/java" />

        <!-- MapperXML生成
        生成SQL map的XML文件生成器，
        注意，在Mybatis3之后，我们可以使用mapper.xml文件+Mapper接口（或者不用mapper接口），
            或者只使用Mapper接口+Annotation，所以，如果 javaClientGenerator配置中配置了需要生成XML的话，这个元素就必须配置
        targetPackage/targetProject:同javaModelGenerator
        -->
        <sqlMapGenerator targetPackage="${generator_sqlMapGenerator_targetPackage}" targetProject="${targetProject_sqlMap}/src/main/java" />

        <!-- Mapper接口生成
        对于mybatis来说，即生成Mapper接口，注意，如果没有配置该元素，那么默认不会生成Mapper接口
        targetPackage/targetProject:同javaModelGenerator
        type：选择怎么生成mapper接口（在MyBatis3/MyBatis3Simple下）：
            1，ANNOTATEDMAPPER：会生成使用Mapper接口+Annotation的方式创建（SQL生成在annotation中），不会生成对应的XML；
            2，MIXEDMAPPER：使用混合配置，会生成Mapper接口，并适当添加合适的Annotation，但是XML会生成在XML中；
            3，XMLMAPPER：会生成Mapper接口，接口完全依赖XML；
        注意，如果context是MyBatis3Simple：只支持ANNOTATEDMAPPER和XMLMAPPER
        -->
        <javaClientGenerator targetPackage="${generator_javaClientGenerator_targetPackage}" targetProject="${targetProject}/src/main/java" type="XMLMAPPER" />

        <!-- 需要映射的表    tables、last_insert_id_tables：MybatisGeneratorUtil.java中的last_insert_id_tables集合 -->
        #foreach($table in $tables)
            #if($last_insert_id_tables.containsKey($!table.table_name) == true)
                <table tableName="$!table.table_name" domainObjectName="$!table.model_name">                                     <!--指定数据库表-->
                    <generatedKey column="$!last_insert_id_tables.get($!table.table_name)" sqlStatement="MySql" identity="true"/><!-- 主键 -->
                </table>
            #else <!-- tableName:用于自动生成代码的数据库表；domainObjectName:对应于数据库表的javaBean类名 -->
                <table tableName="$!table.table_name" domainObjectName="$!table.model_name"></table>
            #end
        #end
    </context>
</generatorConfiguration>
