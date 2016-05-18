package models.JSON;

import java.util.List;

/**
 * Created by giangdaika on 14/05/2016.
 */
public class RelatedItemForm extends  ResultForm{

    List<ItemNameId> itemNameIds;

    public RelatedItemForm() {
    }

    public List<ItemNameId> getItemNameIds() {
        return itemNameIds;
    }

    public void setItemNameIds(List<ItemNameId> itemNameIds) {
        this.itemNameIds = itemNameIds;
    }

}
