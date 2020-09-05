package in.afckstechnologies.attendancemanagement.models;

/**
 * Created by admin on 3/7/2017.
 */

public class MonthNameDAO {
    String id = "";
    String month = "";

    private boolean selected;

    public MonthNameDAO() {

    }

    public MonthNameDAO(String id, String month) {
        this.id = id;
        this.month = month;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return month;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MonthNameDAO) {
            MonthNameDAO c = (MonthNameDAO) obj;
            if (c.getMonth().equals(month) && c.getId() == id) return true;
        }

        return false;
    }
}
