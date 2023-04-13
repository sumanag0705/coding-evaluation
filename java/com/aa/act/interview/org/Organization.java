package com.aa.act.interview.org;

import java.util.Collection;
import java.util.Optional;

public abstract class Organization {

    private Position root;
    
    public Organization() {
       root = createOrganization();
    }
    
    protected abstract Position createOrganization();

    /**
     * hire the given person as an employee in the position that has that title
     * 
     * @param person
     * @param title
     * @return the newly filled position or empty if no position has that title
     */
    public Optional<Position> hire(Name person, String title) {
       Collection<Position> reporties=root.getDirectReports();
       //Optional.of(new Position(title,new Employee(id++,person)));
       Optional<Position> result=null;
       if(root.getTitle().equals(title)){
          root.setEmployee(Optional.of(new Employee(id++, person)));
          return Optional.ofNullable(root);
       }

       for (Position reporty : reporties) {
          if(reporty.getTitle().equals(title)){
             reporty.setEmployee(Optional.of(new Employee(id++, person)));
             return Optional.ofNullable(reporty);
          }
          Optional<Position> position = reporty.getDirectReports().stream()
                .filter(pos -> pos.getTitle().equals(title)).findFirst();
          if (position.isEmpty()) {
             result = Optional.empty();
          } else {
             result= Optional.of(position.get());
             position.get().setEmployee( Optional.of(new Employee(id++, person)));
          }
       }
       return result;
    }

    static int id=1;
    @Override
    public String toString() {
       return printOrganization(root, "");
    }
    
    private String printOrganization(Position pos, String prefix) {
       StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
       for(Position p : pos.getDirectReports()) {
          sb.append(printOrganization(p, prefix + "\t"));
       }
       return sb.toString();
    }
}
