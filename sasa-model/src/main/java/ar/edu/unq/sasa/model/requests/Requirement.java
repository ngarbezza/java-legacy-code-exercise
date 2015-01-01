package ar.edu.unq.sasa.model.requests;

import ar.edu.unq.sasa.model.items.Resource;

public class Requirement {

    private Resource resource;
    private Integer quantity;
    private Boolean optional;

    public Requirement(Resource aResource, Integer aQuantity, Boolean optional) {
        this.resource = aResource;
        this.quantity = aQuantity;
        this.optional = optional;
    }

    public Resource getResource() {
        return resource;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isRequired() {
        return !isOptional();
    }
}
