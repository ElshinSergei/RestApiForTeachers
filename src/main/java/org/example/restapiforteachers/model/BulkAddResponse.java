package org.example.restapiforteachers.model;

import java.util.List;

public class BulkAddResponse {
    int added;
    int failed;
    List<List<String>> errors;

    public BulkAddResponse(int added, int faild, List<List<String>> errors) {
        this.added = added;
        this.failed = faild;
        this.errors = errors;
    }


    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public List<List<String>> getErrors() {
        return errors;
    }

    public void setErrors(List<List<String>> errors) {
        this.errors = errors;
    }

    public int getFaild() {
        return failed;
    }

    public void setFaild(int faild) {
        this.failed = faild;
    }
}
