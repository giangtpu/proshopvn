package models.JSON;

import java.util.List;

/**
 * Created by giangdaika on 14/05/2016.
 */
public class RelatedItemForm {
    boolean success=false;
    List<ItemNameId> itemNameIds;

    public RelatedItemForm() {
    }

    public List<ItemNameId> getItemNameIds() {
        return itemNameIds;
    }

    public void setItemNameIds(List<ItemNameId> itemNameIds) {
        this.itemNameIds = itemNameIds;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
