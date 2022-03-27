package it.polimi.ingsw.model.table;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.TowerColor;

public class Island extends StudentsManager {
private String id;
private int equivalentIsland;
private boolean noEntry;
private TowerColor towerColor;
private boolean tower;

public Island (String id,int equivalentIsland,boolean noEntry,TowerColor towerColor,boolean tower,int maxStudents,int maxStudentsColor )
{
    super(maxStudents,maxStudentsColor);
    this.id=id;
    this.equivalentIsland=equivalentIsland;
    this.noEntry=noEntry;
    this.towerColor=towerColor;
    this.tower=tower;
}

public Island(String id,Island is1,Island is2,int maxStudents,int maxStudentsColor)
{
   super(maxStudents,maxStudentsColor);
   this.id=id;
   this.equivalentIsland=is1.equivalentIsland+is2.equivalentIsland;
   this.noEntry=true;
   this.towerColor=is1.towerColor;
   this.tower=is1.tower;
   addStudentfromSmallIsland(is1);
   addStudentfromSmallIsland(is2);
}

private void addStudentfromSmallIsland(Island island)
{
    for (TeacherColor tc: TeacherColor.values())
    {
        for(int i=0;i<island.howManyStudents(tc);i++)
        {
            this.addStudent(tc);
        }
    }
}

public int howManyEquivalents()
{
    return equivalentIsland;
}

public boolean hasNoEntryTile()
{
    return noEntry;
}

public boolean hasTowers()
{
    return tower;
}

public void setEquivalentIsland(int weight)
{
    this.equivalentIsland=weight;
}

public int getEquivalentIsland()
{
    return equivalentIsland;
}

public void setNoEntry(boolean noEntry)
{
    this.noEntry=noEntry;
}

public boolean getNoEntry()
{
    return noEntry;
}

public void setTowerColor(TowerColor towerColor)
{
    this.towerColor=towerColor;
}

public TowerColor getTowerColor()
{
    return towerColor;
}

public void setTower(boolean tower)
{
    this.tower=tower;
}

public boolean getTower()
{
    return tower;
}
}
