/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\code\\Dzbp\\app\\src\\main\\aidl\\com\\md\\dzbp\\ProcessService.aidl
 */
package com.md.dzbp;
public interface ProcessService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.md.dzbp.ProcessService
{
private static final java.lang.String DESCRIPTOR = "com.md.dzbp.ProcessService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.md.dzbp.ProcessService interface,
 * generating a proxy if needed.
 */
public static com.md.dzbp.ProcessService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.md.dzbp.ProcessService))) {
return ((com.md.dzbp.ProcessService)iin);
}
return new com.md.dzbp.ProcessService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
java.lang.String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_getServiceName:
{
data.enforceInterface(descriptor);
java.lang.String _result = this.getServiceName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.md.dzbp.ProcessService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String getServiceName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getServiceName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getServiceName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public java.lang.String getServiceName() throws android.os.RemoteException;
}
