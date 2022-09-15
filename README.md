# Xiffer Tool

[Dokumentation]() | [Downloads](https://github.com/LostBoyHubble/Xiffer/blob/master/README.md#downloads) | [Patch Notes]() <br>
<br>
<br>

! This release is just a snapshot !

Xiffer is a simple data input output tool which is used to read .xff files. The usage of Xiff (src language) is pretty simple.
```
# I am a comment

* StartingObject:

    variable1 = Hello world!
    int$variable2 = 2022
    long$variable3 = 15092022
    double$variable4 = 15.9
    
    internal_object:
        variable1 = Hello world!
        [...]

* InlineStatement = I am a inline statement
```
Reading the data from the file does require almost no code.
```java
// Parse the file content
ParserResult result = new XiffParser().parse(".../hello/world.xiff");

// Receive and print the file content
String starting_object_v1 = result.content.get("StartingObject/variable1");
String internal_v1 = result.content.get("StartingObject/internal_object/variable1");

System.out.println(starting_object_v1);
System.out.println(internal_v1);
```
It's important to know that you cannot use comments before and after arguments like in `* StartingObject: # Not like this `. This mistake will make your parser crash immediately.
<br>
<br>
<br>
## Downloads

Release Number | Release Date | Release Name | Patch Note | File | File Type | Vulnerabilities
---------------|--------------|--------------|------------|------|-----------|----------------
01 | 15. September 2022 | Xiffer 1.0-SNAPSHOT | [PN]() | [F]() | Library Jar File | 0
