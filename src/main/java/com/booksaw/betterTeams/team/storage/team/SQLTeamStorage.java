package com.booksaw.betterTeams.team.storage.team;

import java.util.List;

import org.bukkit.inventory.Inventory;

import com.booksaw.betterTeams.Team;

public class SQLTeamStorage extends TeamStorage {

	public SQLTeamStorage(Team team) {
		super(team);
	}

	@Override
	protected void setValue(String location, TeamStorageType storageType, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getString(String reference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBoolean(String reference) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getDouble(String reference) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String reference) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getPlayerList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlayerList(List<String> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getBanList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBanList(List<String> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAllyList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAllyList(List<String> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAllyRequestList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAllyRequestList(List<String> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getEchestContents(Inventory inventory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEchestContents(Inventory inventory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getWarps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWarps(List<String> warps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getClaimedChests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClaimedChests(List<String> chests) {
		// TODO Auto-generated method stub
		
	}

}
