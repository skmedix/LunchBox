package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public interface ArmorStand extends LivingEntity {

    ItemStack getItemInHand();

    void setItemInHand(ItemStack itemstack);

    ItemStack getBoots();

    void setBoots(ItemStack itemstack);

    ItemStack getLeggings();

    void setLeggings(ItemStack itemstack);

    ItemStack getChestplate();

    void setChestplate(ItemStack itemstack);

    ItemStack getHelmet();

    void setHelmet(ItemStack itemstack);

    EulerAngle getBodyPose();

    void setBodyPose(EulerAngle eulerangle);

    EulerAngle getLeftArmPose();

    void setLeftArmPose(EulerAngle eulerangle);

    EulerAngle getRightArmPose();

    void setRightArmPose(EulerAngle eulerangle);

    EulerAngle getLeftLegPose();

    void setLeftLegPose(EulerAngle eulerangle);

    EulerAngle getRightLegPose();

    void setRightLegPose(EulerAngle eulerangle);

    EulerAngle getHeadPose();

    void setHeadPose(EulerAngle eulerangle);

    boolean hasBasePlate();

    void setBasePlate(boolean flag);

    boolean hasGravity();

    void setGravity(boolean flag);

    boolean isVisible();

    void setVisible(boolean flag);

    boolean hasArms();

    void setArms(boolean flag);

    boolean isSmall();

    void setSmall(boolean flag);

    boolean isMarker();

    void setMarker(boolean flag);
}
