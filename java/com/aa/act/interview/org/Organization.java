package com.aa.act.interview.org;

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
		return findPosition(root, title)
				.map(position -> {
					if (position.isFilled()) {
						return Optional.<Position>empty();
					}
					Employee employee = new Employee(root.nextId(), person);
					position.setEmployee(Optional.of(employee));
					return Optional.of(position);
				})
				.orElse(Optional.empty());
	}

	private Optional<Position> findPosition(Position position, String title) {
		if (position.getTitle().equals(title)) {
			return Optional.of(position);
		}
		for (Position directReport : position.getDirectReports()) {
			Optional<Position> result = findPosition(directReport, title);
			if (result.isPresent()) {
				return result;
			}
		}
		return Optional.empty();
	}

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
