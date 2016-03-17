# Reactor Playground

Well, let's play with http://projectreactor.io/.
Run [ActionTest](src/test/java/com/github/achmadns/lab/ActionTest.java) for simple demo
or [AppTest](src/test/java/com/github/achmadns/lab/AppTest.java) for spring boot plus camel integration demo.


It is one of many reactive pattern implementation that catch my attention because the people behind it.
Thank you so much Pivotal team. Before you proceed, it is good idea to grasp the idea of being *reactive*
on [reactive manifesto](http://www.reactivemanifesto.org/).
They are:

> - Responsive: The system responds in a timely manner if at all possible. 
Responsiveness is the cornerstone of usability and utility, but more than that, 
responsiveness means that problems may be detected quickly and dealt with effectively. 
Responsive systems focus on providing rapid and consistent response times, 
establishing reliable upper bounds so they deliver a consistent quality of service. 
This consistent behaviour in turn simplifies error handling, builds end user confidence, 
and encourages further interaction.


> - Resilient: The system stays responsive in the face of failure. 
This applies not only to highly-available, mission critical systems — 
any system that is not resilient will be unresponsive after a failure. 
Resilience is achieved by replication, containment, isolation and delegation. 
Failures are contained within each component, isolating components from each other and 
thereby ensuring that parts of the system can fail and recover without compromising the system as a whole. 
Recovery of each component is delegated to another (external) component and high-availability is ensured by 
replication where necessary. The client of a component is not burdened with handling its failures.


> - Elastic: The system stays responsive under varying workload. Reactive Systems can react to changes in the input rate by 
increasing or decreasing the resources allocated to service these inputs. 
This implies designs that have no contention points or central bottlenecks, 
resulting in the ability to shard or replicate components and distribute inputs among them. 
Reactive Systems support predictive, as well as Reactive, scaling algorithms by providing relevant live performance measures. 
They achieve elasticity in a cost-effective way on commodity hardware and software platforms.


> - Message Driven: Reactive Systems rely on asynchronous message-passing to establish a boundary between components 
that ensures loose coupling, isolation, location transparency, and provides the means to delegate errors as messages. 
Employing explicit message-passing enables load management, elasticity, and flow control by shaping and 
monitoring the message queues in the system and applying back-pressure when necessary. 
Location transparent messaging as a means of communication makes it possible for the management of failure to work with 
the same constructs and semantics across a cluster or within a single host. 
Non-blocking communication allows recipients to only consume resources while active, leading to less system overhead.


We'll try to react on something when an error occurred. Suspend the activity until the external resource become available again.
Try to find out which point(s) is applied in our use case.

To simulate mysql problem, execute `schema.sql` first on DB `TEST` (create tabel USER and insert sample data).
The database will connect to local installation on port `3307`, yes, initially it will inaccessible.
Start AppTest.try_user_scheduler() and then after some time run this command to make database accessible

`socat TCP-LISTEN:3307,fork TCP:localhost:3306`

After some time, kill that command and you will see similar to this:
```
2016-03-17 15:03:23 [main] INFO  o.s.t.c.s.AbstractContextLoader - Could not detect default resource locations for test class [com.github.achmadns.lab.AppTest]: no resource found for suffixes {-context.xml, Context.groovy}.
2016-03-17 15:03:23 [main] INFO  o.s.t.c.s.DefaultTestContextBootstrapper - Using TestExecutionListeners: [org.springframework.test.context.support.DependencyInjectionTestExecutionListener@b7dd107]
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.3.3.RELEASE)

2016-03-17 15:03:24 [main] INFO  com.github.achmadns.lab.AppTest - Starting AppTest on achmad-dev with PID 3061 (/home/achmad/repo/examples/reactor-playground/target/test-classes started by achmad in /home/achmad/repo/examples/reactor-playground)
2016-03-17 15:03:24 [main] DEBUG com.github.achmadns.lab.AppTest - Running with Spring Boot v1.3.3.RELEASE, Spring v4.2.5.RELEASE
2016-03-17 15:03:24 [main] INFO  com.github.achmadns.lab.AppTest - No active profile set, falling back to default profiles: default
2016-03-17 15:03:24 [main] INFO  o.s.c.a.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@77888435: startup date [Thu Mar 17 15:03:24 WIB 2016]; root of context hierarchy
2016-03-17 15:03:26 [main] INFO  o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration' of type [class org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration$$EnhancerBySpringCGLIB$$68c06680] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2016-03-17 15:03:26 [main] INFO  o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'org.apache.camel.spring.boot.CamelAutoConfiguration' of type [class org.apache.camel.spring.boot.CamelAutoConfiguration$$EnhancerBySpringCGLIB$$97d4bf] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2016-03-17 15:03:26 [main] DEBUG o.a.camel.spring.SpringCamelContext - Set the application context classloader to: sun.misc.Launcher$AppClassLoader@15db9742
2016-03-17 15:03:26 [main] DEBUG o.a.camel.spring.SpringCamelContext - Set the application context classloader to: sun.misc.Launcher$AppClassLoader@15db9742
2016-03-17 15:03:27 [main] DEBUG o.a.c.i.c.AnnotationTypeConverterLoader - Found 3 packages with 16 @Converter classes to load
2016-03-17 15:03:27 [main] DEBUG o.a.c.i.c.AnnotationTypeConverterLoader - Loading file META-INF/services/org/apache/camel/TypeConverter to retrieve list of packages, from url: jar:file:/home/achmad/.m2/repository/org/apache/camel/camel-spring/2.16.2/camel-spring-2.16.2.jar!/META-INF/services/org/apache/camel/TypeConverter
2016-03-17 15:03:27 [main] DEBUG o.a.c.i.c.AnnotationTypeConverterLoader - Loading file META-INF/services/org/apache/camel/TypeConverter to retrieve list of packages, from url: jar:file:/home/achmad/.m2/repository/org/apache/camel/camel-core/2.16.2/camel-core-2.16.2.jar!/META-INF/services/org/apache/camel/TypeConverter
2016-03-17 15:03:27 [main] DEBUG o.a.c.i.c.AnnotationTypeConverterLoader - Loading file META-INF/services/org/apache/camel/TypeConverter to retrieve list of packages, from url: jar:file:/home/achmad/.m2/repository/org/apache/camel/camel-netty4-http/2.16.2/camel-netty4-http-2.16.2.jar!/META-INF/services/org/apache/camel/TypeConverter
2016-03-17 15:03:27 [main] DEBUG o.a.c.i.c.AnnotationTypeConverterLoader - Loading file META-INF/services/org/apache/camel/TypeConverter to retrieve list of packages, from url: jar:file:/home/achmad/.m2/repository/org/apache/camel/camel-netty4/2.16.2/camel-netty4-2.16.2.jar!/META-INF/services/org/apache/camel/TypeConverter
2016-03-17 15:03:27 [main] DEBUG o.a.c.i.c.AnnotationTypeConverterLoader - Loaded 3 @Converter classes
2016-03-17 15:03:27 [main] INFO  o.a.c.i.c.DefaultTypeConverter - Loaded 197 type converters
2016-03-17 15:03:27 [main] DEBUG o.a.c.impl.SharedProducerServicePool - Starting service pool: org.apache.camel.impl.SharedProducerServicePool@1a411233
2016-03-17 15:03:27 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:240) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:259) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.getFirstUser(DBUtil.java:38) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.getFirstUser(<generated>) [classes/:na]
	at com.github.achmadns.lab.UserScheduler.getFirstUser(UserScheduler.java:31) [classes/:na]
	at com.github.achmadns.lab.UserScheduler.lambda$new$0(UserScheduler.java:26) [classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 41 common frames omitted
2016-03-17 15:03:27 [hash-wheel-timer-run-2] ERROR c.github.achmadns.lab.UserScheduler - Ouch! 
org.sql2o.Sql2oException: An error occurred while executing StatementRunnable
	at org.sql2o.Sql2o.withConnection(Sql2o.java:243) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:259) ~[sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.getFirstUser(DBUtil.java:38) ~[classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) ~[spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) ~[spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.getFirstUser(<generated>) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.getFirstUser(UserScheduler.java:31) [classes/:na]
	at com.github.achmadns.lab.UserScheduler.lambda$new$0(UserScheduler.java:26) [classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: org.sql2o.Sql2oException: Could not acquire a connection from DataSource - Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at org.sql2o.Connection.createConnection(Connection.java:291) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:240) ~[sql2o-1.5.4.jar:na]
	... 16 common frames omitted
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) ~[sql2o-1.5.4.jar:na]
	... 19 common frames omitted
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 41 common frames omitted
2016-03-17 15:03:27 [threadPoolExecutorDispatcher-5] ERROR reactor.bus.EventBus - An error occurred while executing StatementRunnable
org.sql2o.Sql2oException: An error occurred while executing StatementRunnable
	at org.sql2o.Sql2o.withConnection(Sql2o.java:243) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:259) ~[sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.getFirstUser(DBUtil.java:38) ~[classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) ~[spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) ~[spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.getFirstUser(<generated>) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.getFirstUser(UserScheduler.java:31) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.lambda$new$0(UserScheduler.java:26) ~[classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_65]
Caused by: org.sql2o.Sql2oException: Could not acquire a connection from DataSource - Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at org.sql2o.Connection.createConnection(Connection.java:291) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:240) ~[sql2o-1.5.4.jar:na]
	... 16 common frames omitted
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) ~[sql2o-1.5.4.jar:na]
	... 19 common frames omitted
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 41 common frames omitted
2016-03-17 15:03:27 [main] DEBUG o.a.c.spring.boot.RoutesCollector - Post-processing CamelContext bean: camel-1
2016-03-17 15:03:27 [main] DEBUG o.a.c.spring.boot.RoutesCollector - Injecting following route into the CamelContext: Routes: []
2016-03-17 15:03:27 [main] DEBUG o.a.camel.spring.SpringCamelContext - Adding routes from builder: Routes: []
2016-03-17 15:03:27 [main] DEBUG o.a.c.spring.boot.RoutesCollector - Started XML routes detection. Scanning classpath (/camel/*.xml)...
2016-03-17 15:03:27 [main] DEBUG o.a.c.spring.boot.RoutesCollector - No XMl routes found in the classpath (/camel/*.xml). Skipping XML routes detection.
2016-03-17 15:03:27 [main] INFO  o.a.camel.spring.SpringCamelContext - Apache Camel 2.16.2 (CamelContext: camel-1) is starting
2016-03-17 15:03:27 [main] DEBUG o.a.camel.spring.SpringCamelContext - Using ClassResolver=org.apache.camel.impl.DefaultClassResolver@29fc1a2b, PackageScanClassResolver=org.apache.camel.impl.DefaultPackageScanClassResolver@4d0b0fd4, ApplicationContextClassLoader=sun.misc.Launcher$AppClassLoader@15db9742
2016-03-17 15:03:27 [main] INFO  o.a.c.m.ManagedManagementStrategy - JMX is enabled
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Starting JMX agent on server: com.sun.jmx.mbeanserver.JmxMBeanServer@2571066a
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=context,name="camel-1"
2016-03-17 15:03:27 [main] DEBUG o.a.c.support.TimerListenerManager - Added TimerListener: org.apache.camel.management.mbean.ManagedCamelContext@4e2916c3
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementLifecycleStrategy - Registering 2 pre registered services
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=components,name="spring-event"
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultTypeConverter
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultEndpointRegistry
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultExecutorServiceManager
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=SharedProducerServicePool
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=SharedPollingConsumerServicePool
2016-03-17 15:03:27 [main] DEBUG o.a.c.i.SharedPollingConsumerServicePool - Starting service pool: org.apache.camel.impl.SharedPollingConsumerServicePool@629ae7e
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultInflightRepository
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultAsyncProcessorAwaitManager
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultShutdownStrategy
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultPackageScanClassResolver
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultRestRegistry
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultRuntimeEndpointRegistry
2016-03-17 15:03:27 [main] INFO  o.a.c.i.DefaultRuntimeEndpointRegistry - Runtime endpoint registry is in extended mode gathering usage statistics of all incoming and outgoing endpoints (cache limit: 1000)
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=components,name="properties"
2016-03-17 15:03:27 [main] DEBUG o.a.c.p.interceptor.DefaultChannel - Initialize channel for target: 'process[Processor@0x2a9bc08f]'
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=tracer,name=BacklogTracer
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=tracer,name=BacklogDebugger
2016-03-17 15:03:27 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=errorhandlers,name="DefaultErrorHandlerBuilder(ref:CamelDefaultErrorHandlerBuilder)"
2016-03-17 15:03:27 [main] INFO  o.a.camel.spring.SpringCamelContext - AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
2016-03-17 15:03:27 [main] INFO  o.a.camel.spring.SpringCamelContext - StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
2016-03-17 15:03:27 [main] DEBUG o.a.camel.spring.SpringCamelContext - Warming up route id: route1 having autoStartup=true
2016-03-17 15:03:27 [main] DEBUG org.apache.camel.impl.RouteService - Starting services on route: route1
2016-03-17 15:03:27 [main] DEBUG org.apache.camel.impl.RouteService - Starting child service on route: route1 -> Channel[DelegateSync[com.github.achmadns.lab.App$1$$Lambda$4/1956060889@2a9bc08f]]
2016-03-17 15:03:27 [main] DEBUG o.a.c.processor.DefaultErrorHandler - Redelivery enabled: false on error handler: DefaultErrorHandler[Instrumentation:process[DelegateSync[com.github.achmadns.lab.App$1$$Lambda$4/1956060889@2a9bc08f]]]
2016-03-17 15:03:27 [main] DEBUG org.apache.camel.impl.RouteService - Starting child service on route: route1 -> DelegateSync[com.github.achmadns.lab.App$1$$Lambda$4/1956060889@2a9bc08f]
2016-03-17 15:03:28 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=processors,name="process1"
2016-03-17 15:03:28 [main] DEBUG org.apache.camel.impl.RouteService - Starting child service on route: route1 -> Channel[DelegateSync[com.github.achmadns.lab.App$1$$Lambda$4/1956060889@2a9bc08f]]
2016-03-17 15:03:28 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=routes,name="route1"
2016-03-17 15:03:28 [main] DEBUG o.a.c.support.TimerListenerManager - Added TimerListener: org.apache.camel.management.mbean.ManagedSuspendableRoute@49293b43
2016-03-17 15:03:28 [main] DEBUG o.a.camel.spring.SpringCamelContext - Route: route1 >>> EventDrivenConsumerRoute[Endpoint[dummy:in] -> Channel[DelegateSync[com.github.achmadns.lab.App$1$$Lambda$4/1956060889@2a9bc08f]]]
2016-03-17 15:03:28 [main] DEBUG o.a.camel.spring.SpringCamelContext - Starting consumer (order: 1000) on route: route1
2016-03-17 15:03:28 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=consumers,name=Consumer(0x3b05a99b)
2016-03-17 15:03:28 [main] DEBUG com.github.achmadns.lab.App$1$1$2 - Starting consumer: Consumer[dummy:in]
2016-03-17 15:03:28 [main] DEBUG o.a.camel.util.IntrospectionSupport - Configured property: initialDelay on bean: org.apache.camel.impl.DefaultScheduledPollConsumerScheduler@680d4a6a with value: 1000
2016-03-17 15:03:28 [main] DEBUG o.a.camel.util.IntrospectionSupport - Configured property: delay on bean: org.apache.camel.impl.DefaultScheduledPollConsumerScheduler@680d4a6a with value: 500
2016-03-17 15:03:28 [main] DEBUG o.a.camel.util.IntrospectionSupport - Configured property: useFixedDelay on bean: org.apache.camel.impl.DefaultScheduledPollConsumerScheduler@680d4a6a with value: true
2016-03-17 15:03:28 [main] DEBUG o.a.camel.util.IntrospectionSupport - Configured property: scheduledExecutorService on bean: org.apache.camel.impl.DefaultScheduledPollConsumerScheduler@680d4a6a with value: null
2016-03-17 15:03:28 [main] DEBUG o.a.camel.util.IntrospectionSupport - Configured property: timeUnit on bean: org.apache.camel.impl.DefaultScheduledPollConsumerScheduler@680d4a6a with value: MILLISECONDS
2016-03-17 15:03:28 [main] DEBUG o.a.c.m.DefaultManagementAgent - Registered MBean with ObjectName: org.apache.camel:context=camel-1,type=threadpools,name="(0x3b05a99b)"
2016-03-17 15:03:28 [main] DEBUG o.a.c.i.DefaultExecutorServiceManager - Created new ScheduledThreadPool for source: Consumer[dummy:in] with name: dummy:in. -> org.apache.camel.util.concurrent.SizedScheduledExecutorService@51351f28[dummy:in]
2016-03-17 15:03:28 [main] DEBUG o.a.c.i.DefaultScheduledPollConsumerScheduler - Scheduling poll (fixed delay) with initialDelay: 1000, delay: 500 (milliseconds) for: Endpoint[dummy:in]
2016-03-17 15:03:28 [main] INFO  o.a.camel.spring.SpringCamelContext - Route: route1 started and consuming from: Endpoint[dummy:in]
2016-03-17 15:03:28 [main] DEBUG o.a.c.m.DefaultManagementLifecycleStrategy - Load performance statistics disabled
2016-03-17 15:03:28 [main] INFO  o.a.camel.spring.SpringCamelContext - Total 1 routes, of which 1 is started.
2016-03-17 15:03:28 [main] INFO  o.a.camel.spring.SpringCamelContext - Apache Camel 2.16.2 (CamelContext: camel-1) started in 0.365 seconds
2016-03-17 15:03:28 [main] INFO  com.github.achmadns.lab.AppTest - Started AppTest in 3.94 seconds (JVM running for 4.862)
2016-03-17 15:03:28 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:240) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:259) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.getFirstUser(DBUtil.java:38) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.getFirstUser(<generated>) [classes/:na]
	at com.github.achmadns.lab.UserScheduler.getFirstUser(UserScheduler.java:31) [classes/:na]
	at com.github.achmadns.lab.UserScheduler.lambda$new$0(UserScheduler.java:26) [classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 41 common frames omitted
2016-03-17 15:03:28 [hash-wheel-timer-run-2] ERROR c.github.achmadns.lab.UserScheduler - Ouch! 
org.sql2o.Sql2oException: An error occurred while executing StatementRunnable
	at org.sql2o.Sql2o.withConnection(Sql2o.java:243) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:259) ~[sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.getFirstUser(DBUtil.java:38) ~[classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) ~[spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) ~[spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.getFirstUser(<generated>) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.getFirstUser(UserScheduler.java:31) [classes/:na]
	at com.github.achmadns.lab.UserScheduler.lambda$new$0(UserScheduler.java:26) [classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: org.sql2o.Sql2oException: Could not acquire a connection from DataSource - Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at org.sql2o.Connection.createConnection(Connection.java:291) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:240) ~[sql2o-1.5.4.jar:na]
	... 16 common frames omitted
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) ~[sql2o-1.5.4.jar:na]
	... 19 common frames omitted
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 41 common frames omitted
2016-03-17 15:03:28 [threadPoolExecutorDispatcher-7] ERROR reactor.bus.EventBus - An error occurred while executing StatementRunnable
org.sql2o.Sql2oException: An error occurred while executing StatementRunnable
	at org.sql2o.Sql2o.withConnection(Sql2o.java:243) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:259) ~[sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.getFirstUser(DBUtil.java:38) ~[classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) ~[spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) ~[spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.getFirstUser(<generated>) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.getFirstUser(UserScheduler.java:31) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.lambda$new$0(UserScheduler.java:26) ~[classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_65]
Caused by: org.sql2o.Sql2oException: Could not acquire a connection from DataSource - Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at org.sql2o.Connection.createConnection(Connection.java:291) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:240) ~[sql2o-1.5.4.jar:na]
	... 16 common frames omitted
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) ~[sql2o-1.5.4.jar:na]
	... 19 common frames omitted
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 41 common frames omitted
2016-03-17 15:03:28 [threadPoolExecutorDispatcher-7] INFO  com.github.achmadns.lab.AppTest - Problem with data access!
2016-03-17 15:03:28 [threadPoolExecutorDispatcher-7] INFO  com.github.achmadns.lab.AppTest - DB check resumed.
2016-03-17 15:03:29 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:29 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:29 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:280) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:268) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.check(DBUtil.java:27) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.check(<generated>) [classes/:na]
	at com.github.achmadns.lab.AppTest.lambda$try_user_scheduler$10(AppTest.java:147) [test-classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 40 common frames omitted
2016-03-17 15:03:29 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:29 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:29 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:30 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:30 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:30 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:280) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:268) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.check(DBUtil.java:27) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.check(<generated>) [classes/:na]
	at com.github.achmadns.lab.AppTest.lambda$try_user_scheduler$10(AppTest.java:147) [test-classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 40 common frames omitted
2016-03-17 15:03:30 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:30 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:30 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:31 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:31 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:31 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:280) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:268) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.check(DBUtil.java:27) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.check(<generated>) [classes/:na]
	at com.github.achmadns.lab.AppTest.lambda$try_user_scheduler$10(AppTest.java:147) [test-classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 40 common frames omitted
2016-03-17 15:03:31 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:31 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:31 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:32 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:32 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:32 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:280) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:268) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.check(DBUtil.java:27) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.check(<generated>) [classes/:na]
	at com.github.achmadns.lab.AppTest.lambda$try_user_scheduler$10(AppTest.java:147) [test-classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 40 common frames omitted
2016-03-17 15:03:32 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:32 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:32 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:33 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:33 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:33 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:280) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:268) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.check(DBUtil.java:27) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.check(<generated>) [classes/:na]
	at com.github.achmadns.lab.AppTest.lambda$try_user_scheduler$10(AppTest.java:147) [test-classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 40 common frames omitted
2016-03-17 15:03:33 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:33 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:33 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:34 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:34 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:34 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:280) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:268) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.check(DBUtil.java:27) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.check(<generated>) [classes/:na]
	at com.github.achmadns.lab.AppTest.lambda$try_user_scheduler$10(AppTest.java:147) [test-classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 40 common frames omitted
2016-03-17 15:03:34 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:34 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:34 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:35 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:35 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:35 [hash-wheel-timer-run-2] ERROR o.a.tomcat.jdbc.pool.ConnectionPool - Unable to create initial connections of pool.
com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:339) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2253) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:2286) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:2085) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:795) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.JDBC4Connection.<init>(JDBC4Connection.java:44) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:400) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:327) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connectUsingDriver(PooledConnection.java:307) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.PooledConnection.connect(PooledConnection.java:200) ~[tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.createConnection(ConnectionPool.java:708) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.borrowConnection(ConnectionPool.java:642) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.init(ConnectionPool.java:464) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.ConnectionPool.<init>(ConnectionPool.java:141) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.pCreatePool(DataSourceProxy.java:115) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.createPool(DataSourceProxy.java:102) [tomcat-jdbc-8.0.32.jar:na]
	at org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(DataSourceProxy.java:126) [tomcat-jdbc-8.0.32.jar:na]
	at org.sql2o.Connection.createConnection(Connection.java:288) [sql2o-1.5.4.jar:na]
	at org.sql2o.Connection.<init>(Connection.java:51) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.open(Sql2o.java:225) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:280) [sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:268) [sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.check(DBUtil.java:27) [classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) [classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) [spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) [spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) [spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.check(<generated>) [classes/:na]
	at com.github.achmadns.lab.AppTest.lambda$try_user_scheduler$10(AppTest.java:147) [test-classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: java.net.ConnectException: Connection refused
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_65]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_65]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_65]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_65]
	at com.mysql.jdbc.StandardSocketFactory.connect(StandardSocketFactory.java:211) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.<init>(MysqlIO.java:298) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 40 common frames omitted
2016-03-17 15:03:35 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:35 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:35 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:36 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:36 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:36 [threadPoolExecutorDispatcher-5] INFO  com.github.achmadns.lab.AppTest - user scheduler started
2016-03-17 15:03:36 [threadPoolExecutorDispatcher-5] INFO  com.github.achmadns.lab.AppTest - DB check paused.
2016-03-17 15:03:36 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB is connected.
2016-03-17 15:03:36 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:36 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:36 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:37 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:37 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:37 [threadPoolExecutorDispatcher-11] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:37 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:37 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:38 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:38 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:38 [threadPoolExecutorDispatcher-8] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:38 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:38 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:39 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:39 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:39 [threadPoolExecutorDispatcher-13] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:39 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:39 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:40 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:40 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:40 [threadPoolExecutorDispatcher-10] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:40 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:40 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:41 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:41 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:41 [threadPoolExecutorDispatcher-7] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:41 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:41 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:42 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:42 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:42 [threadPoolExecutorDispatcher-12] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:42 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:42 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:43 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:43 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:43 [threadPoolExecutorDispatcher-9] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:43 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:43 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:44 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:44 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:44 [threadPoolExecutorDispatcher-5] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:44 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:44 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:45 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:45 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:45 [threadPoolExecutorDispatcher-11] INFO  com.github.achmadns.lab.AppTest - Got com.github.achmadns.lab.User@b4b1a2ae
2016-03-17 15:03:45 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:45 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:46 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:46 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:46 [hash-wheel-timer-run-2] ERROR c.github.achmadns.lab.UserScheduler - Ouch! 
org.sql2o.Sql2oException: An error occurred while executing StatementRunnable
	at org.sql2o.Sql2o.withConnection(Sql2o.java:243) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:259) ~[sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.getFirstUser(DBUtil.java:38) ~[classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) ~[spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) ~[spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.getFirstUser(<generated>) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.getFirstUser(UserScheduler.java:31) [classes/:na]
	at com.github.achmadns.lab.UserScheduler.lambda$new$0(UserScheduler.java:26) [classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) ~[na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) ~[na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) ~[na:1.8.0_65]
Caused by: org.sql2o.Sql2oException: Database error: Communications link failure

The last packet successfully received from the server was 1,000 milliseconds ago.  The last packet sent successfully to the server was 1 milliseconds ago.
	at org.sql2o.Query$ResultSetIterableBase.<init>(Query.java:332) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Query$10.<init>(Query.java:412) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Query.executeAndFetchLazy(Query.java:412) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Query.executeAndFetchFirst(Query.java:480) ~[sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.lambda$getFirstUser$1(DBUtil.java:39) ~[classes/:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:241) ~[sql2o-1.5.4.jar:na]
	... 16 common frames omitted
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet successfully received from the server was 1,000 milliseconds ago.  The last packet sent successfully to the server was 1 milliseconds ago.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.reuseAndReadPacket(MysqlIO.java:3465) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.reuseAndReadPacket(MysqlIO.java:3365) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3805) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2478) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2625) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2551) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.PreparedStatement.executeInternal(PreparedStatement.java:1861) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.PreparedStatement.executeQuery(PreparedStatement.java:1962) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.sql2o.Query$ResultSetIterableBase.<init>(Query.java:328) ~[sql2o-1.5.4.jar:na]
	... 21 common frames omitted
Caused by: java.io.EOFException: Can not read response from server. Expected to read 4 bytes, read 0 bytes before connection was unexpectedly lost.
	at com.mysql.jdbc.MysqlIO.readFully(MysqlIO.java:2957) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.reuseAndReadPacket(MysqlIO.java:3375) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 29 common frames omitted
2016-03-17 15:03:46 [threadPoolExecutorDispatcher-8] ERROR reactor.bus.EventBus - An error occurred while executing StatementRunnable
org.sql2o.Sql2oException: An error occurred while executing StatementRunnable
	at org.sql2o.Sql2o.withConnection(Sql2o.java:243) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:259) ~[sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.getFirstUser(DBUtil.java:38) ~[classes/:na]
	at com.github.achmadns.lab.DBUtil$$FastClassBySpringCGLIB$$ef366022.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204) ~[spring-core-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:720) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136) ~[spring-tx-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:655) ~[spring-aop-4.2.5.RELEASE.jar:4.2.5.RELEASE]
	at com.github.achmadns.lab.DBUtil$$EnhancerBySpringCGLIB$$b0f0e252.getFirstUser(<generated>) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.getFirstUser(UserScheduler.java:31) ~[classes/:na]
	at com.github.achmadns.lab.UserScheduler.lambda$new$0(UserScheduler.java:26) ~[classes/:na]
	at reactor.fn.timer.HashWheelTimer$TimerPausable.run(HashWheelTimer.java:325) ~[reactor-core-2.0.7.RELEASE.jar:na]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_65]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_65]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_65]
Caused by: org.sql2o.Sql2oException: Database error: Communications link failure

The last packet successfully received from the server was 1,000 milliseconds ago.  The last packet sent successfully to the server was 1 milliseconds ago.
	at org.sql2o.Query$ResultSetIterableBase.<init>(Query.java:332) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Query$10.<init>(Query.java:412) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Query.executeAndFetchLazy(Query.java:412) ~[sql2o-1.5.4.jar:na]
	at org.sql2o.Query.executeAndFetchFirst(Query.java:480) ~[sql2o-1.5.4.jar:na]
	at com.github.achmadns.lab.DBUtil.lambda$getFirstUser$1(DBUtil.java:39) ~[classes/:na]
	at org.sql2o.Sql2o.withConnection(Sql2o.java:241) ~[sql2o-1.5.4.jar:na]
	... 16 common frames omitted
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure

The last packet successfully received from the server was 1,000 milliseconds ago.  The last packet sent successfully to the server was 1 milliseconds ago.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_65]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_65]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_65]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:422) ~[na:1.8.0_65]
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:404) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:981) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.reuseAndReadPacket(MysqlIO.java:3465) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.reuseAndReadPacket(MysqlIO.java:3365) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3805) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2478) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2625) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2551) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.PreparedStatement.executeInternal(PreparedStatement.java:1861) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.PreparedStatement.executeQuery(PreparedStatement.java:1962) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at org.sql2o.Query$ResultSetIterableBase.<init>(Query.java:328) ~[sql2o-1.5.4.jar:na]
	... 21 common frames omitted
Caused by: java.io.EOFException: Can not read response from server. Expected to read 4 bytes, read 0 bytes before connection was unexpectedly lost.
	at com.mysql.jdbc.MysqlIO.readFully(MysqlIO.java:2957) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	at com.mysql.jdbc.MysqlIO.reuseAndReadPacket(MysqlIO.java:3375) ~[mysql-connector-java-5.1.38.jar:5.1.38]
	... 29 common frames omitted
2016-03-17 15:03:46 [threadPoolExecutorDispatcher-8] INFO  com.github.achmadns.lab.AppTest - Problem with data access!
2016-03-17 15:03:46 [threadPoolExecutorDispatcher-8] INFO  com.github.achmadns.lab.AppTest - DB check resumed.
2016-03-17 15:03:46 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:46 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:47 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:47 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:47 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:47 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:47 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:48 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:48 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:48 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:48 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:48 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:49 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:49 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:49 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:49 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:49 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:50 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:50 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:50 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:50 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:50 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:51 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:51 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:51 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:51 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:51 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:52 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:52 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:52 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:52 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:52 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:53 [hash-wheel-timer-run-2] INFO  com.github.achmadns.lab.AppTest - DB checked.
2016-03-17 15:03:53 [Camel (camel-1) thread #0 - dummy:in] INFO  c.github.achmadns.lab.ResourceAspect - Resource aspect got result will be executed.
2016-03-17 15:03:53 [Camel (camel-1) thread #0 - dummy:in] INFO  com.github.achmadns.lab.App$1 - ABC was processed.
2016-03-17 15:03:53 [Thread-7] INFO  o.s.c.a.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@77888435: startup date [Thu Mar 17 15:03:24 WIB 2016]; root of context hierarchy
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.i.SharedPollingConsumerServicePool - Stopping service pool: org.apache.camel.impl.SharedPollingConsumerServicePool@629ae7e
2016-03-17 15:03:53 [Thread-7] INFO  o.a.camel.spring.SpringCamelContext - Apache Camel 2.16.2 (CamelContext: camel-1) is shutting down
2016-03-17 15:03:53 [Thread-7] INFO  o.a.c.impl.DefaultShutdownStrategy - Starting to graceful shutdown 1 routes (timeout 300 seconds)
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.i.DefaultExecutorServiceManager - Created new ThreadPool for source: org.apache.camel.impl.DefaultShutdownStrategy@2ca532af with name: ShutdownTask. -> org.apache.camel.util.concurrent.RejectableThreadPoolExecutor@68d693c3[Running, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0][ShutdownTask]
2016-03-17 15:03:53 [Camel (camel-1) thread #1 - ShutdownTask] DEBUG o.a.c.impl.DefaultShutdownStrategy - There are 1 routes to shutdown
2016-03-17 15:03:53 [Camel (camel-1) thread #1 - ShutdownTask] INFO  com.github.achmadns.lab.App$1$1$2 - Consumer was suspended.
2016-03-17 15:03:53 [Camel (camel-1) thread #1 - ShutdownTask] DEBUG o.a.c.impl.DefaultShutdownStrategy - Route: route1 suspended and shutdown deferred, was consuming from: Endpoint[dummy:in]
2016-03-17 15:03:53 [Camel (camel-1) thread #1 - ShutdownTask] DEBUG o.a.c.i.DefaultExecutorServiceManager - Forcing shutdown of ExecutorService: org.apache.camel.util.concurrent.SizedScheduledExecutorService@51351f28[dummy:in]
2016-03-17 15:03:53 [Camel (camel-1) thread #1 - ShutdownTask] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=threadpools,name="(0x3b05a99b)"
2016-03-17 15:03:53 [Camel (camel-1) thread #1 - ShutdownTask] DEBUG com.github.achmadns.lab.App$1$1$2 - Stopping consumer: Consumer[dummy:in]
2016-03-17 15:03:53 [Camel (camel-1) thread #1 - ShutdownTask] INFO  o.a.c.impl.DefaultShutdownStrategy - Route: route1 shutdown complete, was consuming from: Endpoint[dummy:in]
2016-03-17 15:03:53 [Thread-7] INFO  o.a.c.impl.DefaultShutdownStrategy - Graceful shutdown of 1 routes completed in 0 seconds
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.i.DefaultAsyncProcessorAwaitManager - Shutting down with no inflight threads.
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.support.TimerListenerManager - Removed TimerListener: org.apache.camel.management.mbean.ManagedSuspendableRoute@49293b43
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=routes,name="route1"
2016-03-17 15:03:53 [Thread-7] DEBUG org.apache.camel.impl.RouteService - Stopping services on route: route1
2016-03-17 15:03:53 [Thread-7] DEBUG org.apache.camel.impl.RouteService - Shutting down child service on route: route1 -> Consumer[dummy:in]
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=consumers,name=Consumer(0x3b05a99b)
2016-03-17 15:03:53 [Thread-7] DEBUG org.apache.camel.impl.RouteService - Shutting down child service on route: route1 -> Channel[DelegateSync[com.github.achmadns.lab.App$1$$Lambda$4/1956060889@2a9bc08f]]
2016-03-17 15:03:53 [Thread-7] DEBUG org.apache.camel.impl.RouteService - Shutting down child service on route: route1 -> DelegateSync[com.github.achmadns.lab.App$1$$Lambda$4/1956060889@2a9bc08f]
2016-03-17 15:03:53 [Thread-7] DEBUG org.apache.camel.impl.RouteService - Shutting down child service on route: route1 -> Channel[DelegateSync[com.github.achmadns.lab.App$1$$Lambda$4/1956060889@2a9bc08f]]
2016-03-17 15:03:53 [Thread-7] DEBUG org.apache.camel.impl.RouteService - Shutting down services on route: route1
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.support.TimerListenerManager - Removed TimerListener: org.apache.camel.management.mbean.ManagedCamelContext@2d3c8116
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=context,name="camel-1"
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.i.DefaultExecutorServiceManager - Forcing shutdown of ExecutorService: org.apache.camel.util.concurrent.RejectableThreadPoolExecutor@68d693c3[Running, pool size = 1, active threads = 0, queued tasks = 0, completed tasks = 1][ShutdownTask]
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.impl.DefaultInflightRepository - Shutting down with no inflight exchanges.
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.impl.SharedProducerServicePool - Stopping service pool: org.apache.camel.impl.SharedProducerServicePool@1a411233
2016-03-17 15:03:53 [Thread-7] DEBUG org.apache.camel.util.EventHelper - Ignoring notifying event Stopped CamelContext: camel-1. The EventNotifier has not been started yet: org.apache.camel.impl.DefaultRuntimeEndpointRegistry@1dbd15c5
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=SharedPollingConsumerServicePool
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=tracer,name=BacklogTracer
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=processors,name="process1"
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=components,name="properties"
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultEndpointRegistry
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=SharedProducerServicePool
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultRuntimeEndpointRegistry
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultAsyncProcessorAwaitManager
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultInflightRepository
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultTypeConverter
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=tracer,name=BacklogDebugger
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultShutdownStrategy
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultPackageScanClassResolver
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=components,name="spring-event"
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultExecutorServiceManager
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=services,name=DefaultRestRegistry
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.c.m.DefaultManagementAgent - Unregistered MBean with ObjectName: org.apache.camel:context=camel-1,type=errorhandlers,name="DefaultErrorHandlerBuilder(ref:CamelDefaultErrorHandlerBuilder)"
2016-03-17 15:03:53 [Thread-7] DEBUG o.a.camel.util.IntrospectionSupport - Clearing cache[size=48, hits=59, misses=48, evicted=0]
2016-03-17 15:03:53 [Thread-7] INFO  o.a.camel.spring.SpringCamelContext - Apache Camel 2.16.2 (CamelContext: camel-1) uptime 25.545 seconds
2016-03-17 15:03:53 [Thread-7] INFO  o.a.camel.spring.SpringCamelContext - Apache Camel 2.16.2 (CamelContext: camel-1) is shutdown in 0.021 seconds

Process finished with exit code 130

```