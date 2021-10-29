package com.mygdx.nextlevel.JUnitTests;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.nextlevel.actors.Block;
import com.mygdx.nextlevel.actors.Player;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    Block block;
    Player player;

    final short bottom = 1;
    final short left = 2;
    final short top = 3;
    final short right = 4;

    @Before
    public void init() {
        player = new Player(new Vector2(32, 32), 0.25f, 0f);
        block = new Block(new Vector2(200, 32), (short) 0x0000, false);
    }

    @After
    public void clear() {
        player = null;
        block = null;

        TestOutputHelper.displayResult();
        TestOutputHelper.clearResult();
    }

    @Test
    @Description("Test to check Block class has collisions set on only bottom")
    public void testCollisionBottom() {
        short expected = (short) 0b0001;
        block.setCollision((short)(0x1 << (bottom - 1)));
        short actual = block.getCollision();
        TestOutputHelper.setResult("testCollisionBottom", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check Block class has collisions set on only left")
    public void testCollisionLeft() {
        short expected = (short) 0b0010;
        block.setCollision((short)(0x1 << (left - 1)));
        short actual = block.getCollision();
        TestOutputHelper.setResult("testCollisionLeft", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check Block class has collisions set on only top")
    public void testCollisionTop() {
        short expected = (short) 0b0100;
        block.setCollision((short)(0x1 << (top - 1)));
        short actual = block.getCollision();
        TestOutputHelper.setResult("testCollisionTop", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check Block class has collisions set on only right")
    public void testCollisionRight() {
        short expected = (short) 0b1000;
        block.setCollision((short)(0x1 << (right - 1)));
        short actual = block.getCollision();
        TestOutputHelper.setResult("testCollisionRight", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check Block class has collisions set on only the Sides (Left/Right)")
    public void testCollisionLeftRight() {
        short expected = (short) 0b1010;
        block.setCollision((short) ((0x1 << (right - 1)) | (0x1 << (left - 1))));
        short actual = block.getCollision();
        TestOutputHelper.setResult("testCollisionLeftRight", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check Block class has collisions set on only the Sides (Left/Right/Bottom)")
    public void testCollisionLeftRightBottom() {
        short expected = (short) 0b1011;
        block.setCollision((short) ((0x1 << (right - 1)) | (0x1 << (left - 1)) | (0x1 << (bottom - 1))));
        short actual = block.getCollision();
        TestOutputHelper.setResult("testCollisionLeftRight", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    @Description("Test to check Block class has collisions set on only the Sides (Top/Bottom)")
    public void testCollisionTopBottom() {
        short expected = (short) 0b0101;
        block.setCollision((short) ((0x1 << (top - 1)) | (0x1 << (bottom - 1))));
        short actual = block.getCollision();
        TestOutputHelper.setResult("testCollisionTopBottom", expected, actual);
        assertEquals(expected, actual);
    }
}