/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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