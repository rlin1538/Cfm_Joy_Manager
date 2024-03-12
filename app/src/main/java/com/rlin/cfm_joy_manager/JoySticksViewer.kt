package com.rlin.cfm_joy_manager

import ImgHelper.getBitmapFormResources
import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.scale
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rlin.cfm_joy_manager.entity.JoyButtonProperty
import com.rlin.cfm_joy_manager.utils.getDrawableId
import com.rlin.cfm_joy_manager.utils.standardJoySticksJson
import java.lang.reflect.Type


@Composable
fun JoySticksViewer(
    json: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val joys: HashMap<String, JoyButtonProperty>
    val typeMyType: Type = object : TypeToken<HashMap<String, JoyButtonProperty>?>() {}.type

    joys = Gson().fromJson(json, typeMyType)

    val standardJoys: HashMap<String, JoyButtonProperty> =
        Gson().fromJson(standardJoySticksJson, typeMyType)

    fun getScaledJoyImg(btnName: String): Bitmap {
        var img =
            getBitmapFormResources(context, getDrawableId(context, btnName.lowercase()))
        img = img.scale(
            ((img.width / standardJoys[btnName]!!.Scale) * joys[btnName]!!.Scale).toInt(),
            ((img.height / standardJoys[btnName]!!.Scale) * joys[btnName]!!.Scale).toInt()
        )
        return img
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color(60, 60, 60)),
        onDraw = {
            val scaleXFactor: Float = size.width / 1200
            val scaleYFactor: Float = size.height / 640
            val alphaFactor = 0.7f
            val edgeOffset = 30F

            drawLine(
                color = Color.Red,
                start = Offset(size.width / 2, 20F),
                end = Offset(size.width / 2, size.height - 20F),
                alpha = 0.5F
            )
            drawLine(
                color = Color.Red,
                start = Offset(20F, size.height / 2),
                end = Offset(size.width - 20F, size.height / 2),
                alpha = 0.5F
            )

            // 左上
            val leftTop =
                arrayOf(
                    "PauseSettingButton",
                    "RadarSwitchButton",
                    "StaticWalkBtn",
                    "MapButtonNew"
                )
            leftTop.forEach { btnName ->
                if (joys[btnName] != null) {
                    val img = getScaledJoyImg(btnName)
                    drawImage(
                        image = img.asImageBitmap(),
                        topLeft = Offset(
                            edgeOffset + (scaleXFactor * joys[btnName]!!.xPos - (img.width / 2)),
                            ((-scaleYFactor * joys[btnName]!!.yPos) - (img.height / 2))
                        ),
                        alpha = alphaFactor * joys[btnName]!!.Alpha
                    )
                }
            }

            // 左中
            val leftCenter =
                arrayOf(
                    "RestoreGrenadeIdleStateBtn",
                    "CloseSniperZoomButton",
                    "ChangeSniperFOVView",
                    "LeftFireButton",
                    "ChangeSniperZoomButton",
                    "LeftJumpButton",
                )
            leftCenter.forEach { btnName ->
                if (joys[btnName] != null) {
                    val img = getScaledJoyImg(btnName)
                    drawImage(
                        image = img.asImageBitmap(),
                        topLeft = Offset(
                            edgeOffset + (scaleXFactor * joys[btnName]!!.xPos - (img.width / 2)),
                            (size.height / 2) + ((-scaleYFactor * joys[btnName]!!.yPos) - (img.height / 2))
                        ),
                        alpha = alphaFactor * joys[btnName]!!.Alpha
                    )
                }
            }

            // 左下
            val leftDown =
                arrayOf(
                    "MovementJoystick",
                    "UserCrouchButton",
                    "UserCrawlButton",
                    "C4Btn"
                )
            leftDown.forEach { btnName ->
                if (joys[btnName] != null) {
                    val img = getScaledJoyImg(btnName)
                    drawImage(
                        image = img.asImageBitmap(),
                        topLeft = Offset(
                            edgeOffset + (scaleXFactor * joys[btnName]!!.xPos - (img.width / 2)),
                            size.height + ((-scaleYFactor * joys[btnName]!!.yPos) - (img.height / 2))
                        ),
                        alpha = alphaFactor * joys[btnName]!!.Alpha
                    )
                }
            }

            // 居中
            val center =
                arrayOf(
                    "InGameVoiceMicroPhoneButton",
                    "BombC4Button",
                    "DroppedPickUpConfirmButton",
                )
            center.forEach { btnName ->
                if (joys[btnName] != null) {
                    val img = getScaledJoyImg(btnName)
                    drawImage(
                        image = img.asImageBitmap(),
                        topLeft = Offset(
                            (size.width / 2) + (scaleXFactor * joys[btnName]!!.xPos - (img.width / 2)),
                            (size.height / 2) + ((-scaleYFactor * joys[btnName]!!.yPos) - (img.height / 2))
                        ),
                        alpha = alphaFactor * joys[btnName]!!.Alpha
                    )
                }
            }

            // 中底
            val centerDown =
                arrayOf(
                    "PlayerInfoHUD",
                    "LookAtGunButton",
                    "ReAmmoButton",
                    "QuickSwitchWeaponButton",
                    "SwitchBagButton",
                    "SmileButton"
                )
            centerDown.forEach { btnName ->
                if (joys[btnName] != null) {
                    val img = getScaledJoyImg(btnName)
                    drawImage(
                        image = img.asImageBitmap(),
                        topLeft = Offset(
                            (size.width / 2) + (scaleXFactor * joys[btnName]!!.xPos - (img.width / 2)),
                            (size.height) + ((-scaleYFactor * joys[btnName]!!.yPos) - (img.height / 2))
                        ),
                        alpha = alphaFactor * joys[btnName]!!.Alpha
                    )
                }
            }

            // 右上
            val rightTop =
                arrayOf(
                    "InGameVoiceTalkerButton",
                    "WeaponInfoButton",
                )
            rightTop.forEach { btnName ->
                if (joys[btnName] != null) {
                    val img = getScaledJoyImg(btnName)
                    drawImage(
                        image = img.asImageBitmap(),
                        topLeft = Offset(
                            (size.width) + (scaleXFactor * joys[btnName]!!.xPos - (img.width / 2)),
                            ((-scaleYFactor * joys[btnName]!!.yPos) - (img.height / 2))
                        ),
                        alpha = alphaFactor * joys[btnName]!!.Alpha
                    )
                }
            }
            // 右中
            val rightCenter =
                arrayOf(
                    "GrenadeButton",
                    "JumpButton",
                    "LiudanGunButton",
                    "WeaponWangZheZhiXinBtn",
                    "WeaponQingTianBtn",
                    "SecondaryAttackButton",
                )
            rightCenter.forEach { btnName ->
                if (joys[btnName] != null) {
                    println(btnName)
                    val img = getScaledJoyImg(btnName)
                    drawImage(
                        image = img.asImageBitmap(),
                        topLeft = Offset(
                            (size.width) + (scaleXFactor * joys[btnName]!!.xPos - (img.width / 2)),
                            (size.height / 2) + ((-scaleYFactor * joys[btnName]!!.yPos) - (img.height / 2))
                        ),
                        alpha = alphaFactor * joys[btnName]!!.Alpha
                    )
                }

            }

            // 右下
            val rightDown =
                arrayOf(
                    "FixedFireButton",
//                    "FollowFireButton",
                )
            rightDown.forEach { btnName ->
                if (joys[btnName] != null) {
                    val img = getScaledJoyImg(btnName)
                    drawImage(
                        image = img.asImageBitmap(),
                        topLeft = Offset(
                            (size.width) + (scaleXFactor * joys[btnName]!!.xPos - (img.width / 2)),
                            (size.height) + ((-scaleYFactor * joys[btnName]!!.yPos) - (img.height / 2))
                        ),
                        alpha = alphaFactor * joys[btnName]!!.Alpha
                    )
                }
            }
        }
    )
}
