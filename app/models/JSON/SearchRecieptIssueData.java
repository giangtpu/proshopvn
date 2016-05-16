package models.JSON;

import models.RecieptIssue;
import models.forms.SearchFilterForm;

import java.util.List;

/**
 * Created by giangdaika on 16/05/2016.
 */
public class SearchRecieptIssueData {
    protected List<RecieptIssue> recieptIssueList;
    private SearchFilterForm searchFilterForm;

    public SearchRecieptIssueData() {
    }

    public List<RecieptIssue> getRecieptIssueList() {
        return recieptIssueList;
    }

    public void setRecieptIssueList(List<RecieptIssue> recieptIssueList) {
        this.recieptIssueList = recieptIssueList;
    }

    public SearchFilterForm getSearchFilterForm() {
        return searchFilterForm;
    }

    public void setSearchFilterForm(SearchFilterForm searchFilterForm) {
        this.searchFilterForm = searchFilterForm;
    }
}
