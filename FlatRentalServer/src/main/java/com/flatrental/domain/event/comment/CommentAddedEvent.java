package com.flatrental.domain.event.comment;

import org.springframework.context.ApplicationEvent;

public class CommentAddedEvent<T> extends ApplicationEvent {

    private final T relatedObject;

    public CommentAddedEvent(Object source, T relatedObject) {
        super(source);
        this.relatedObject = relatedObject;
    }

    public T getRelatedObject() {
        return relatedObject;
    }

}
