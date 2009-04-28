package server.universe;

/**
 * Outline of item weapon that can be equipped to creatures.
 * @author Simon
 *
 */
public class Weapon implements Item {

	@Override
	public int getPrice() {
		return 5;
	}

	@Override
	public String getDescription() {
		return "Attack enemies with extra power";
	}

	@Override
	public String getName() {
		return "dagger";
	}

}
