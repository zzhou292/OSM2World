package org.osm2world.core.world.modules;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openstreetmap.josm.plugins.graphview.core.data.MapBasedTagGroup;
import org.openstreetmap.josm.plugins.graphview.core.data.Tag;
import org.openstreetmap.josm.plugins.graphview.core.data.TagGroup;
import org.osm2world.core.map_data.data.MapArea;
import org.osm2world.core.map_data.data.MapNode;
import org.osm2world.core.math.VectorXZ;
import org.osm2world.core.test.TestMapDataGenerator;
import org.osm2world.core.world.modules.BuildingModule.BuildingPart;
import org.osm2world.core.world.modules.BuildingModule.Wall;

public class BuildingModuleTest {

	@Test
	public void testSplitIntoWalls() {

		TestMapDataGenerator generator = new TestMapDataGenerator();

		List<MapNode> nodes = new ArrayList<>(asList(
				generator.createNode(new VectorXZ(-10, -5)),
				generator.createNode(new VectorXZ(  0, -5)),
				generator.createNode(new VectorXZ(+10, -5)),
				generator.createNode(new VectorXZ(+10, +5)),
				generator.createNode(new VectorXZ(-10, +5))
				));

		nodes.add(nodes.get(0));

		MapArea buildingPartArea = generator.createWayArea(nodes, new MapBasedTagGroup(new Tag("building", "yes")));

		/* test the basic case */

		List<Wall> result = BuildingPart.splitIntoWalls(buildingPartArea, null);

		assertEquals(4, result.size());

		assertEquals(nodes.subList(0, 3), result.get(0).getNodes());
		assertEquals(nodes.subList(2, 4), result.get(1).getNodes());
		assertEquals(nodes.subList(3, 5), result.get(2).getNodes());
		assertEquals(nodes.subList(4, 6), result.get(3).getNodes());

		/* add a building:wall=yes way and test again */

		generator.createWay(asList(nodes.get(1), nodes.get(0), nodes.get(5)),
				new MapBasedTagGroup(new Tag("building:wall", "yes")));

		result = BuildingPart.splitIntoWalls(buildingPartArea, null);

		assertEquals(5, result.size());

		assertEquals(nodes.subList(0, 2), result.get(0).getNodes());
		assertEquals(nodes.subList(1, 3), result.get(1).getNodes());
		assertEquals(nodes.subList(2, 4), result.get(2).getNodes());
		assertEquals(nodes.subList(3, 5), result.get(3).getNodes());
		assertEquals(nodes.subList(4, 6), result.get(4).getNodes());

	}

	@Test
	public void testInheritTags() {

		TagGroup ownTags = new MapBasedTagGroup(asList(
				new Tag("key0", "valA"),
				new Tag("key1", "valB")));

		TagGroup parentTags = new MapBasedTagGroup(asList(
				new Tag("key1", "valX"),
				new Tag("key2", "valY")));

		TagGroup result = BuildingModule.inheritTags(ownTags, parentTags);

		assertEquals(3, result.size());
		assertEquals("valA", result.getValue("key0"));
		assertEquals("valB", result.getValue("key1"));
		assertEquals("valY", result.getValue("key2"));

	}

}
