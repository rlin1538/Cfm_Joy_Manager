package com.rlin.cfm_joy_manager.entity

data class JoyButtons(
    // 左上
    val PauseSettingButton: JoyButtonProperty,
    val RadarSwitchButton: JoyButtonProperty,
    val StaticWalkBtn: JoyButtonProperty,
    val MapButtonNew: JoyButtonProperty,
    // 左中
    val RestoreGrenadeIdleStateBtn: JoyButtonProperty,
    val CloseSniperZoomButton: JoyButtonProperty,
    val ChangeSniperFOVView: JoyButtonProperty,
    val LeftFireButton: JoyButtonProperty,
    val ChangeSniperZoomButton: JoyButtonProperty,
    val LeftJumpButton: JoyButtonProperty,
    // 左下
    val MovementJoystick: JoyButtonProperty,
    val UserCrouchButton: JoyButtonProperty,
    val UserCrawlButton: JoyButtonProperty,
    val C4Btn: JoyButtonProperty,
    // 中间
    val InGameVoiceMicroPhoneButton: JoyButtonProperty,
    val BombC4Button: JoyButtonProperty,
//    val WeaponThrowSuiteSwitchBtn: JoyButtonProperty,
    val DroppedPickUpConfirmButton: JoyButtonProperty,
    // 中下
    val PlayerInfoHUD: JoyButtonProperty,
    val LookAtGunButton: JoyButtonProperty,
    val ReAmmoButton: JoyButtonProperty,
    val QuickSwitchWeaponButton: JoyButtonProperty,
    val SwitchBagButton: JoyButtonProperty,
    val SmileButton: JoyButtonProperty,
    // 右上
    val InGameVoiceTalkerButton: JoyButtonProperty,
    val WeaponInfoButton: JoyButtonProperty,
    // 右中
    val GrenadeButton: JoyButtonProperty,
    val JumpButton: JoyButtonProperty,
    val LiudanGunButton: JoyButtonProperty,
    val WeaponWangZheZhiXinBtn: JoyButtonProperty,
    val WeaponQingTianBtn: JoyButtonProperty,
    val SecondaryAttackButton: JoyButtonProperty,
    // 右下
    val FixedFireButton: JoyButtonProperty,
    val FollowFireButton: JoyButtonProperty,
) {
}