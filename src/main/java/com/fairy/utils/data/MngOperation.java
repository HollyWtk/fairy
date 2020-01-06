package com.fairy.utils.data;

public interface MngOperation {

	MngResponse Export(MngOperatePacket packet);

	MngResponse Import(MngOperatePacket packet);

	MngResponse Add(MngOperatePacket packet);

	MngResponse Delete(MngOperatePacket packet);

	MngResponse Edit(MngOperatePacket packet);

	MngResponse List(MngOperatePacket packet);

	MngResponse Detail(MngOperatePacket packet);

	MngResponse CheckUnique(MngOperatePacket packet);

	MngResponse FieldSource(MngOperatePacket packet);

    MngResponse exportTem(MngOperatePacket packet);

    MngResponse exportErrorMsg(MngOperatePacket packet);

}
