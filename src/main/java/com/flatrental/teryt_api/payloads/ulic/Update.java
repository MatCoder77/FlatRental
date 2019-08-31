package com.flatrental.teryt_api.payloads.ulic;


public class Update<K, V> {

    private K entityBeforeUpdate;
    private V stateAfterUpdate;

    public Update(K entityBeforeUpdate, V stateAfterUpdate) {
        this.entityBeforeUpdate = entityBeforeUpdate;
        this.stateAfterUpdate = stateAfterUpdate;
    }

    public K getEntityBeforeUpdate() {
        return entityBeforeUpdate;
    }

    public V getStateAfterUpdate() {
        return stateAfterUpdate;
    }

}
