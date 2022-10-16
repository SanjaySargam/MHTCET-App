package Models;

public class CategoryModel {
    private final String docId;
    private String name;
    private final int noOfSets;

    public CategoryModel(String docId, String name, int noOfSets) {
        this.docId = docId;
        this.name = name;
        this.noOfSets = noOfSets;
    }

    public String getDocId() {
        return docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfSets() {
        return noOfSets;
    }

}
