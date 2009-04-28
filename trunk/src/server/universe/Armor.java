package server.universe;

/**
 * Armor can be equipped to creatures.
 * @author Simon
 *
 */
public class Armor implements Item {

	@Override
	public int getPrice() {
		return 5;
	}

	@Override
	public String getDescription() {
		return "Protect yourself from enemy attacks";
	}

	@Override
	public String getName() {
		return "leather";
	}

}
