package com.rlin.cfm_joy_manager

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rlin.cfm_joy_manager.ui.theme.Cfm_Joy_ManagerTheme
import com.rlin.cfm_joy_manager.utils.CFM_DIR
import com.rlin.cfm_joy_manager.utils.REQUEST_CODE_FOR_DIR
import com.rlin.cfm_joy_manager.utils.changeToUri3
import com.rlin.cfm_joy_manager.utils.getJoyFiles
import com.rlin.cfm_joy_manager.utils.hasDirectionPermission
import com.rlin.cfm_joy_manager.utils.startFor
import com.rlin.cfm_joy_manager.widget.PermissionDialog
import kotlin.properties.Delegates


class MainActivity : ComponentActivity() {
    private lateinit var mNavController: NavHostController
    private val mActivity = this

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showDialog by remember {
                mutableStateOf(!hasDirectionPermission(contentResolver))
            }
            Log.d("MainActivity", "是否弹出权限申请弹窗：${showDialog}")

            Cfm_Joy_ManagerTheme {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            if (hasDirectionPermission(contentResolver)) {
                                showDialog = false
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    startFor(CFM_DIR, mActivity, REQUEST_CODE_FOR_DIR)
                                    if (hasDirectionPermission(contentResolver)) {
                                        showDialog = false
                                    }
                                }
                            ) {
                                Text("同意")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    finish()
                                }
                            ) {
                                Text("不同意")
                            }
                        },
                        title = { Text("授权提示") },
                        text = { Text("请同意授权才能使用应用") },
                    )
                } else {
                    Greeting()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri: Uri? = data?.data

        if (requestCode == REQUEST_CODE_FOR_DIR) {
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it, (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                )
            } //关键是这里，这个就是保存这个目录的访问权限
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun Greeting(modifier: Modifier = Modifier) {
        val context = LocalContext.current

        mNavController = rememberNavController()
        NavHost(navController = mNavController, startDestination = "page_home") {
            composable("page_home") {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        Column {
                            ElevatedButton(
                                onClick = {
                                    val json = """
                                     {"UserCrouchButton":{"xPos":345.9017,"yPos":423.357239,"Scale":1.381124,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":0.282326,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"UserCrawlButton":{"xPos":148,"yPos":46,"Scale":1.1,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"MovementJoystick":{"xPos":248.7541,"yPos":170.754333,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":true,"VerticalLimit":false,"BtnType":0},"RotationJoystick":null,"FixedFireButton":{"xPos":-166.050751,"yPos":117.816833,"Scale":0.822863,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":false,"UpSide":true,"HorizontalLimit":true,"VerticalLimit":false,"BtnType":0},"FollowFireButton":{"xPos":-308.687317,"yPos":219.056427,"Scale":0.944955,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":false,"UpSide":true,"HorizontalLimit":true,"VerticalLimit":false,"BtnType":0},"MoveFireButton":null,"LeftFireButton":{"xPos":49,"yPos":-21,"Scale":1.2,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"FixedFireButtonForFixedMoveFire":null,"FixedMovementForFixedMoveFire":null,"JumpButton":{"xPos":-47,"yPos":-10,"Scale":1.2,"MaxScale":5,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"LeftJumpButton":{"xPos":297.5251,"yPos":264.07132,"Scale":3.288159,"MaxScale":5,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":0.105709,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"CrouchButton":{"xPos":345.9017,"yPos":423.357239,"Scale":1.381124,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":0.282326,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"CrouchButtonReverse":null,"CrawlButton":{"xPos":148,"yPos":46,"Scale":1.1,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"CrawlButtonReverse":null,"WeaponInfoButton":{"xPos":-91,"yPos":-70,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ReAmmoButton":{"xPos":267.1317,"yPos":45.9959221,"Scale":1.663281,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ReAmmoButtonForBigMelee":null,"SmileButton":{"xPos":510,"yPos":500,"Scale":1.2,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"RightHand":{"xPos":0,"yPos":0,"Scale":0.6,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":0.6,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"SwitchBagButton":{"xPos":-150,"yPos":38,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"CloseSniperZoomButton":{"xPos":66,"yPos":36,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ChangeSniperZoomButton":{"xPos":66,"yPos":-46,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"GrenadeButton":{"xPos":-37,"yPos":77,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"HeavyAttackButton":null,"QuickSwitchWeaponButton":{"xPos":379.927368,"yPos":54.4960823,"Scale":2.075521,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"PlayerInfoHUD":{"xPos":-269,"yPos":30,"Scale":0.7,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ItemButton1":null,"ItemButton2":{"xPos":-116,"yPos":77,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":0.8,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"AutoFireModeButton":{"xPos":68.68585,"yPos":-493.765045,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"StateSettingButton":null,"InGameVoiceMicroPhoneButton":{"xPos":320.5112,"yPos":209.929886,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"InGameVoiceTalkerButton":{"xPos":-248,"yPos":-15,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"SecondaryAttackButton":{"xPos":-136.911346,"yPos":-121.383614,"Scale":1.771097,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":0.258001,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ModeSwtichButton":{"xPos":-40,"yPos":-97,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"DroppedPickUpConfirmButton":{"xPos":0,"yPos":-150,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"RecordBtn":null,"StaticWalkBtn":{"xPos":215,"yPos":-197,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"RepeatShowSwitch":{"xPos":-42.0043335,"yPos":-162.5162,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"AvatarAttackBtn":{"xPos":-185,"yPos":-160,"Scale":0.9,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"WeaponQingTianBtn":{"xPos":-77.57968,"yPos":-138.242371,"Scale":1.028064,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"WeaponWangZheZhiXinBtn":{"xPos":-127,"yPos":-3,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"WeaponThrowSuiteSwitchBtn":{"xPos":-138,"yPos":115,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"RestoreGrenadeIdleStateBtn":{"xPos":66,"yPos":36,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"SettingHud":null,"C4Btn":{"xPos":69,"yPos":221,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ChangeSniperFOVView":{"xPos":210,"yPos":0,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"SkillButton1":{"xPos":-171.25,"yPos":81.171875,"Scale":1.3,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"SkillButton2":null,"SkillButton3":null,"SkillButton4":null,"JumpRacingGameWatchButton":{"xPos":-160,"yPos":75,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"JumpRacingGameResetButton":{"xPos":-50,"yPos":75,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ViewControlButton":null,"ForwardBackButton":null,"RightwardBackButton":null,"MPSkillButton":null,"SpecifiedActionButton":null,"ClimbButton":null,"ClimbDropDownButton":null,"BuffStateView":null,"LiudanGunButton":{"xPos":-120,"yPos":0,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"BioSkillButton":{"xPos":372.636841,"yPos":50.4824562,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"BioSkillButton2":{"xPos":400,"yPos":400,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":0.8,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ChangeCrossHairButton":null,"LookAtGunButton":{"xPos":430,"yPos":500,"Scale":1.2,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ThrowAccuracyButton":{"xPos":-77,"yPos":77,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"PenQiButton":{"xPos":-531,"yPos":-283,"Scale":0.8,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"WeaponGadgetSkillButton":{"xPos":381,"yPos":330,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"PauseSettingButton":{"xPos":37,"yPos":-33,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"RadarSwitchButton":{"xPos":200,"yPos":-24,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"SnowBallPowerProgressBar":null,"SnowBallGameBigSnowBallBtn":null,"SnowBallGameBigJumpBtn":null,"HuLuWaGameSkillBtn":null,"HuLuWaGameSelectBtn":null,"BigTeamGameSkillBtn":{"xPos":-120,"yPos":150,"Scale":1.3,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"FightBackCatGameAttachBtn":null,"FightBackCatGameUnAttachBtn":null,"FightBackCatGameHumanBtn":null,"FightBackCatGameCatBtn":null,"FightBackCatGameObjBtn":null,"FightBackCatGameCatJokeBtn":null,"WeightlessHeroBtn":null,"WeightlessHookBtn":null,"WeightlessRecoverBtn":null,"BioChaseGameItemBtn":null,"BazookaJumpBtn":{"xPos":-320,"yPos":80,"Scale":1,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"ColdWeaponSkillBtn":null,"GlassMeleeJumpBtn":{"xPos":-230,"yPos":70,"Scale":1,"MaxScale":4,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"MapButtonNew":{"xPos":110,"yPos":-94,"Scale":1,"MaxScale":1.5,"MinScale":0.5,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0},"BombC4Button":{"xPos":-160,"yPos":-48.7,"Scale":1,"MaxScale":2,"MinScale":0.6,"Scale2":1,"MaxScale2":2,"MinScale2":0.6,"Alpha":1,"LeftSide":true,"UpSide":true,"HorizontalLimit":false,"VerticalLimit":false,"BtnType":0}}
                                 """.trimIndent()
                                    mNavController.navigate("Page_viewer/$json")
                                }
                            ) {
                                Text(text = "测试")
                            }
                            ElevatedButton(
                                onClick = {
                                    startFor(CFM_DIR, mActivity, REQUEST_CODE_FOR_DIR)
                                }
                            ) {
                                Text("获取权限")
                            }
                            ElevatedButton(
                                onClick = {
                                    val documentFile: DocumentFile? = DocumentFile.fromTreeUri(
                                        context, Uri.parse(
                                            changeToUri3(CFM_DIR)
                                        )
                                    )
                                    documentFile?.let { it1 -> getJoyFiles(it1) }
                                }
                            ) {
                                Text("获取键位数据")
                            }
                        }
                    }
                }
            }
            composable(route = "page_viewer/{json}",
                arguments = listOf<NamedNavArgument>(
                    navArgument("json") {
                        type = NavType.StringType
                        defaultValue = "null"
                    }
                )
            ) { it ->
                JoySticksViewerPage(it)
            }
            composable(route = "page_joys") {

            }
        }
    }

    @Composable
    fun JoySticksViewerPage(it: NavBackStackEntry) {
        val configuration = LocalConfiguration.current
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(configuration, context, lifecycleOwner) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            onDispose {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }
        val json = it.arguments?.getString("json")
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Gray
        ) {
            json?.let { it1 -> JoySticksViewer(it1) }
        }
    }
}

