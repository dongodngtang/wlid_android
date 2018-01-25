package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * Created by zengjiang on 16/6/12.
 */
public class ItemEquipment {
    private String name;
    private boolean isSelected=false;

    public ItemEquipment(String name) {
        this.name = name;
    }

    public ItemEquipment(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemEquipment that = (ItemEquipment) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
