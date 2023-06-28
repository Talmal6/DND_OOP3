package dnd.Backend;

import java.util.ArrayList;
import java.util.List;

public class Report {
    public List<Unit> units;

    public Report(List<Unit> units){
        this.units = units;
    }

    public Report(Unit unit){
        this.units = new ArrayList<>();
        this.units.add(unit);
    }

    public Report(){
        this.units = null;
    }
}
