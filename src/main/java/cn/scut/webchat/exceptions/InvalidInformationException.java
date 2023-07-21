package cn.scut.webchat.exceptions;

public class InvalidInformationException extends Exception{

    public InvalidInformationException(String message) {
        super(message + "回去改改吧");
    }
}
