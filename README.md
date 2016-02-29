# org.kie.server.thrift

Kie Server Apache Thrift Extension

License

I license this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Introduction

The Kie Server Apache Trift extension uses the extension mechanism provided by the Kie Server version 6.4.0 to add support for the Apache Trift TCompactProtocol for the ReST transport, only Thrift (de)serialization is used. Thrift is a lightweight, language-independent software stack with an associated code generation mechanism for RPC. Thrift provides clean abstractions for data transport, data serialization, and application level processing. The code generation system takes a simple definition language as its input and generates code across programming languages. Thrift is specifically designed to support non-atomic version changes across client and server code.


Requirements

Maven 3.0.5

Modified Thrift maven plugin / Thrift compiler 0.9.2

Tested on JavaEE7 with Wildfly 8.2.1 using Kie Server 6.4.0.Beta2


Features

Very compact binary message payloads with low overhead (de)serialization resulting in high speed messaging with language bindings that feel natural

Object oriented, no schemas or data bindings, less boilerplate code

Internal Kie Server use of Java Objects like BigDecimal or Date for all Thrift supported languages



Acknowledgments

Hmmdeutschland GmbH for investing worktime into the project

Maciej Swiderski for his informative blog http://mswiderski.blogspot.de

Alexander Knyn for being my php integration sparring partner
