namespace php hmm.zhponline.base.thrift.java
namespace java org.kie.server.thrift.java

const string JAVA_MODEL_NAMESPACE = "org.kie.server.thrift.java"

 // Java model

 struct BigDecimal {
    1: required string value;
 }

 struct BigInteger {
     1: required string value;
     2: required i32 radix = 10
  }

 struct Date {
    1: required i64 value;
 }

 struct Calendar {
    1: required i32 year;
    2: required i32 month;
    3: required i32 day_of_month;
    4: optional i32 hour_of_day = 0
    5: optional i32 minute = 0
    6: optional i32 second = 0
 }