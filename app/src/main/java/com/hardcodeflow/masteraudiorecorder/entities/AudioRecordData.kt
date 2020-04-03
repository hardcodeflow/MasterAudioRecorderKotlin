package com.hardcodeflow.masteraudiorecorder.entities

class AudioRecordData {
    var audioRecordId:Int=0
    var audioRecordFilePath: String=""
    var audioRecordName:String=" Record Name "
    var audioRecordFullName: String=""
    var audioRecordDirectory:String=""
    var audioRecordExtension:String=""

    var audioRecordDateTime: String=""
    constructor(audioRecordFilePath:String){

        this.audioRecordFilePath=audioRecordFilePath

        audioRecordDirectory = audioRecordFilePath.substringBeforeLast("/")
        audioRecordFullName = audioRecordFilePath.substringAfterLast("/")
        audioRecordName = audioRecordFullName.substringBeforeLast(".")
        audioRecordExtension = audioRecordFullName.substringAfterLast(".")
    }
}