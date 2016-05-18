package models.JSON;

import models.Promotion;
import models.forms.SearchFilterForm;

import java.util.List;

/**
 * Created by giangdaika on 18/05/2016.
 */
public class SearchPromotionData {
    protected List<Promotion> promotionList;
    private SearchFilterForm searchFilterForm;

    public SearchPromotionData() {
    }

    public List<Promotion> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    public SearchFilterForm getSearchFilterForm() {
        return searchFilterForm;
    }

    public void setSearchFilterForm(SearchFilterForm searchFilterForm) {
        this.searchFilterForm = searchFilterForm;
    }
}
