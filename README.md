# org.kie.server.thrift

Kie Server Apache Thrift Extension

License

Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements. See the NOTICE file
distributed with this work for additional information
regarding copyright ownership. The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied. See the License for the
specific language governing permissions and limitations
under the License.
 
Introduction

The Kie Server Apache Thrift extension uses the extension mechanism provided by the Kie Server version 6.4.0 to add support for the Apache Thrift TCompactProtocol for the ReST transport, therefore only Thrift (de)serialization is used. Thrift is a lightweight, language-independent software stack with an associated code generation mechanism for RPC. Thrift provides clean abstractions for data transport, data serialization, and application level processing. The code generation system takes a simple definition language as its input and generates code across programming languages. Thrift is specifically designed to support non-atomic version changes across client and server code.


Requirements

Maven 3.0.5

Modified Thrift maven plugin / Thrift compiler 0.9.2

Tested on JavaEE7 with Wildfly 8.2.1 using Kie Server 6.4.0.CR1


Features

Very compact binary message payloads with low (de)serialization overhead, resulting in high speed messaging with language bindings that feel natural

Object oriented, no schemas or data bindings, less boilerplate code

Automatic internal Kie Server use of Java Objects like BigDecimal or Date for all Thrift supported languages on none wrapped structs. Wrapped structs can be converted into there Java type and back again by using the static methods of the converter helper classes.



Acknowledgments

Hmmdeutschland GmbH for investing worktime into the project

Maciej Swiderski for his informative blog http://mswiderski.blogspot.de

Alexander Knyn https://de.linkedin.com/in/alexander-knyn-26472095, for being my php integration sparring partner
