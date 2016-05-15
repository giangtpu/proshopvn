package models.JSON;


import models.Item;
import models.forms.SearchFilterForm;

import java.util.List;

/**
 * Created by minhnt31 on 01/03/2016.
 */
public class SearchItemData {
    protected List<Item> itemList;
    private SearchFilterForm searchFilterForm;


    public SearchFilterForm getSearchFilterForm() {
        return searchFilterForm;
    }

    public void setSearchFilterForm(SearchFilterForm searchFilterForm) {
        this.searchFilterForm = searchFilterForm;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
