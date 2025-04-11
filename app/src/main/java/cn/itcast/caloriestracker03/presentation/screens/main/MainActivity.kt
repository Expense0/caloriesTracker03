package cn.itcast.caloriestracker03.presentation.screens.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import cn.itcast.caloriestracker03.CaloriesTracker03
import cn.itcast.caloriestracker03.data.UserSettingsManager
import cn.itcast.caloriestracker03.data.localRoom.DatabaseProvider
import cn.itcast.caloriestracker03.data.localRoom.dao.MealDao
import cn.itcast.caloriestracker03.data.remote.OpenFoodApiService
import cn.itcast.caloriestracker03.data.repositoryImpl.CaloriesCounterRepositoryImpl
import cn.itcast.caloriestracker03.data.repositoryImpl.FoodProductRepositoryImpl
import cn.itcast.caloriestracker03.data.repositoryImpl.MealRepositoryImpl
import cn.itcast.caloriestracker03.data.repositoryImpl.UserRepositoryImpl
import cn.itcast.caloriestracker03.domain.usecase.calorie_calculator_validation.ValidateInfoForCalculation
import cn.itcast.caloriestracker03.domain.usecase.meal_validation.ValidateMeal
import cn.itcast.caloriestracker03.domain.usecase.user_info_validation.ValidateActivityLevel
import cn.itcast.caloriestracker03.domain.usecase.user_info_validation.ValidateAge
import cn.itcast.caloriestracker03.domain.usecase.user_info_validation.ValidateGender
import cn.itcast.caloriestracker03.domain.usecase.user_info_validation.ValidateGoal
import cn.itcast.caloriestracker03.domain.usecase.user_info_validation.ValidateHeight
import cn.itcast.caloriestracker03.domain.usecase.user_info_validation.ValidateWeight
import cn.itcast.caloriestracker03.presentation.screens.analytics.AnalyticsViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.AnalyticsViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal.AddMealViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal.AddMealViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.CameraViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.CameraViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.analytics.edit_meal.EditMealViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.edit_meal.EditMealViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail.MealDetailViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail.MealDetailViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.analytics.search_product.SearchProductViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.search_product.SearchProductViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.home.HomeViewModel
import cn.itcast.caloriestracker03.presentation.screens.home.HomeViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.onboarding.OnboardingViewModel
import cn.itcast.caloriestracker03.presentation.screens.onboarding.OnboardingViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.settings.SettingsViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.SettingsViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator.CaloriesCalculatorViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator.CaloriesCalculatorViewModelFactory
import cn.itcast.caloriestracker03.presentation.screens.settings.daily_calorie_intake.DailyCalorieIntakeViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.daily_calorie_intake.DailyCalorieIntakeViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import cn.itcast.caloriestracker03.presentation.theme.CaloriesTracker03Theme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    //创建dao
    private lateinit var mealDao: MealDao

    //创建单例viewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var onboardingViewModel: OnboardingViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var analyticsViewModel: AnalyticsViewModel
    private lateinit var addMealViewModel: AddMealViewModel
//    private lateinit var searchProductViewModel: SearchProductViewModel
    private lateinit var editMealViewModel: EditMealViewModel
    private lateinit var mealDetailViewModel: MealDetailViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var caloriesCalculatorViewModel: CaloriesCalculatorViewModel
    private lateinit var dailyCalorieIntakeViewModel: DailyCalorieIntakeViewModel
    private lateinit var cameraViewModel: CameraViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //上下文
        val context : Context = CaloriesTracker03.getAppContext()
        // 初始化数据库
        val db = DatabaseProvider.getDatabase(this)
        //初始化dao
        mealDao = db.mealDao()
// 创建 Retrofit 实例
        val retrofit = Retrofit.Builder()
            .baseUrl(OpenFoodApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

// 创建 OpenFoodApiService 的实现类实例
        val openFoodApiService = retrofit.create(OpenFoodApiService::class.java)

        //创建Repository的单例
        val userRepository = UserRepositoryImpl.getInstance(application)
        val mealRepository = MealRepositoryImpl.getInstance(mealDao)
        val foodProductRepository = FoodProductRepositoryImpl.getInstance(openFoodApiService)
        val caloriesCounterRepository = CaloriesCounterRepositoryImpl.getInstance()
        val userSettingsManager = UserSettingsManager.getInstance(context)

        //创建用例
        val validateAge = ValidateAge()
        val validateWeight = ValidateWeight()
        val validateHeight = ValidateHeight()
        val validateGoal = ValidateGoal()
        val validateActivityLevel = ValidateActivityLevel()
        val validateGender = ValidateGender()

        val validateInfoForCalculation = ValidateInfoForCalculation()
        val validateMeal = ValidateMeal()

        // 使用工厂创建 MainViewModel
        val factoryMain = MainViewModelFactory(userRepository, userSettingsManager)
        mainViewModel = ViewModelProvider(this, factoryMain)[MainViewModel::class.java]

        val factoryOnboarding = OnboardingViewModelFactory(
            application,
            userRepository,
            caloriesCounterRepository,
            validateAge,
            validateWeight,
            validateHeight,
            validateGoal,
            validateActivityLevel,
            validateGender
        )
        onboardingViewModel = ViewModelProvider(this, factoryOnboarding)[OnboardingViewModel::class.java]

        val factoryHome = HomeViewModelFactory(mealRepository,userRepository)
        homeViewModel = ViewModelProvider(this,factoryHome)[HomeViewModel::class.java]

        val factoryAddMeal = AddMealViewModelFactory(mealRepository,validateMeal,application)
        addMealViewModel = ViewModelProvider(this,factoryAddMeal)[AddMealViewModel::class.java]

//        val factorySearchProduct = SearchProductViewModelFactory(foodProductRepository)
//        searchProductViewModel = ViewModelProvider(this,factorySearchProduct)[SearchProductViewModel::class.java]

        val factoryCamera = CameraViewModelFactory(foodProductRepository)
        cameraViewModel = ViewModelProvider(this,factoryCamera)[CameraViewModel::class.java]

        val factoryEditMeal = EditMealViewModelFactory(mealRepository, foodProductRepository,validateMeal,application)
        editMealViewModel = ViewModelProvider(this,factoryEditMeal)[EditMealViewModel::class.java]

        val factoryMealDetail = MealDetailViewModelFactory(mealRepository)
        mealDetailViewModel = ViewModelProvider(this,factoryMealDetail)[MealDetailViewModel::class.java]

        val factoryAnalytics = AnalyticsViewModelFactory(userRepository,mealRepository)
        analyticsViewModel = ViewModelProvider(this,factoryAnalytics)[AnalyticsViewModel::class.java]

        val factorySettings = SettingsViewModelFactory(userSettingsManager)
        settingsViewModel = ViewModelProvider(this, factorySettings)[SettingsViewModel::class.java]

        val factoryCaloriesCalculatorViewModelFactory = CaloriesCalculatorViewModelFactory(
            calorieCounterRepository = caloriesCounterRepository,
            userRepository = userRepository,
            validateInfoForCalculation = validateInfoForCalculation,
            application = application
        )
        caloriesCalculatorViewModel = ViewModelProvider(this,factoryCaloriesCalculatorViewModelFactory)[CaloriesCalculatorViewModel::class.java]

        val factoryDailyCalorieIntakeViewModelFactory = DailyCalorieIntakeViewModelFactory(
            calorieCounterRepository = caloriesCounterRepository,
            userRepository = userRepository,
        )
        dailyCalorieIntakeViewModel = ViewModelProvider(this,factoryDailyCalorieIntakeViewModelFactory)[DailyCalorieIntakeViewModel::class.java]

        installSplashScreen().apply {
            this.setKeepOnScreenCondition { mainViewModel.state is MainScreenState.Initial }
        }
        setContent {
            val isDarkTheme = mainViewModel.isDarkTheme.collectAsState(initial = false)
            CaloriesTracker03Theme(darkTheme = isDarkTheme.value) {

                val systemUiController = rememberSystemUiController().apply {
                    setSystemBarsColor(MaterialTheme.colorScheme.background)
                }

                if (mainViewModel.state != MainScreenState.Initial) {
                    Box(Modifier.fillMaxSize()) {
                        MainScreen(
                            mainViewModel = mainViewModel,
                            onboardingViewModel = onboardingViewModel,
                            settingsViewModel = settingsViewModel,
                            caloriesCalculatorViewModel = caloriesCalculatorViewModel,
                            dailyCalorieIntakeViewModel = dailyCalorieIntakeViewModel,
                            analyticsViewModel = analyticsViewModel,
                            addMealViewModel = addMealViewModel,
                            cameraViewModel = cameraViewModel,
//                            searchProductViewModel = searchProductViewModel,
                            editMealViewModel = editMealViewModel,
                            mealDetailViewModel = mealDetailViewModel,
                            homeViewModel = homeViewModel
                        )
                    }
                }
            }
        }
    }

}

