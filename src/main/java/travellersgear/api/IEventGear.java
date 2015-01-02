package travellersgear.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public interface IEventGear
{
	public void onUserDamaged(LivingHurtEvent event, ItemStack stack);
	public void onUserAttacking(AttackEntityEvent event, ItemStack stack);
	public void onUserJump(LivingJumpEvent event, ItemStack stack);
	public void onUserFall(LivingFallEvent event, ItemStack stack);
	public void onUserTargeted(LivingSetAttackTargetEvent event, ItemStack stack);
}