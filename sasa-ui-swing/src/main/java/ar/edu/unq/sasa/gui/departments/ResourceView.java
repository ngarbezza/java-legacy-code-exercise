package ar.edu.unq.sasa.gui.departments;

import ar.edu.unq.sasa.model.items.Resource;

/**
 * Modelo de aplicación para representar recursos con su cantidad, en un pedido.
 * TODO maybe not needed anymore after Requirement class is present
 */
public class ResourceView {

    private Resource resource;
    private Integer amount;

    public ResourceView(Resource aResource, int anAmount) {
        resource = aResource;
        amount = anAmount;
    }

    public void setResource(Resource aResource) {
        resource = aResource;
    }

    public Resource getResource() {
        return resource;
    }

    public void setAmount(int anAmount) {
        amount = anAmount;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getName() {
        return getResource().getName();
    }
}
