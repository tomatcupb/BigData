# MapReduce
1. Writable: hadoop自己开发的一套精简高效的序列化机制
    - Java自带的序列化是一个重量级序列化框架（Serializable），一个对象被序列化后，会附带很多额外的信息（各种校验信息，header，继承体系等），不便于在网络中高效传输。