package com.flatrental.domain.event.comment;

import org.springframework.context.ApplicationEvent;

import java.util.Collection;

public class CommentRemovedEvent<T> extends ApplicationEvent {

    private final T relatedObject;
    private final Collection<Long> removedComments;

    public CommentRemovedEvent(Object source, T relatedObject, Collection<Long> removedComments) {
        super(source);
        this.relatedObject = relatedObject;
        this.removedComments = removedComments;
    }

    public T getRelatedObject() {
        return relatedObject;
    }

    public Collection<Long> getRemovedComments() {
        return removedComments;
    }

}
