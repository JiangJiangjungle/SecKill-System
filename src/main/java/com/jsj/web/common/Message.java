package com.jsj.web.common;

import com.jsj.util.ServiceResult;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message<T> {

    private Head head;

    private T body;

    public Message(ServiceResult resultEnum, T body) {
        this.head = new Head();
        this.head.setStatusCode(resultEnum.getValue());
        this.head.setStatusMessage(resultEnum.getLabel());
        this.body = body;
    }

    public Message(ServiceResult resultEnum) {
        this.head = new Head();
        this.head.setStatusCode(resultEnum.getValue());
        this.head.setStatusMessage(resultEnum.getLabel());
    }

    public Message(T body) {
        this.body = body;
    }

    public Message(Head head, T body) {
        this.head = head;
        this.body = body;
    }
}
