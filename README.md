<div align="center"><img src="logo.png" style="zoom:75%;" /></div>

# fzOS-SDK
Java-wrapped SDK for fzOS.

Due to the requirements of "full emulation", A Java agent class is required.

## Design thought

Because of Java' s class loading & linking methodology, we can use runtime "Hooks" to implement our Kernel API.
In other words, loads an emulated class at development time, and provides another set of classes at runtime.
The annotation "fzos.FzOSInternalImplementation" is a mark for implementation differential.