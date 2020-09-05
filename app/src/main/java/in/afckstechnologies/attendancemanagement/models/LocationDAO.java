package in.afckstechnologies.attendancemanagement.models;

/**
 * Created by admin on 3/7/2017.
 */

public class LocationDAO {
    String id = "";
    String branch_name = "";

    private boolean selected;

    public LocationDAO() {

    }

    public LocationDAO(String id, String branch_name) {
        this.id = id;
        this.branch_name = branch_name;

    }

    @Override
    public String toString() {
        return branch_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocationDAO) {
            LocationDAO c = (LocationDAO) obj;
            if (c.getBranch_name().equals(branch_name) && c.getId() == id) return true;
        }

        return false;
    }
}
