package com.example.shopdrop.di

import android.app.Application
import android.content.Context
import com.example.shopdrop.data.firestore.FireStoreOperations
import com.example.shopdrop.data.repository.*
import com.example.shopdrop.domain.repository.*
import com.example.shopdrop.domain.user_case.auth_user.*
import com.example.shopdrop.domain.user_case.get_product.GetProductUseCase
import com.example.shopdrop.domain.user_case.get_products.GetProductsUseCase
import com.example.shopdrop.domain.user_case.get_user.GetUserUseCase
import com.example.shopdrop.domain.user_case.get_users.GetUsersUseCase
import com.example.shopdrop.domain.user_case.update_address.UpdateAddressUseCase
import com.example.shopdrop.domain.user_case.update_profile.UpdateProfileUseCase
import com.example.shopdrop.domain.user_case.update_user_cart.UpdateUserCartUseCase
import com.example.shopdrop.domain.user_case.update_user_wishlist.UpdateUserWishlistUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideFireBaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    @Provides
    @Singleton
    fun provideProductRepository(firebaseFirestore: FirebaseFirestore): ProductRepository {
        return ProductRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): StorageReference {
        return Firebase.storage.reference
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        context: Context,
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore,
        reference: StorageReference
    ): AuthRepository {
        return AuthRepositoryImpl(context, firebaseAuth, fireStore, reference)
    }

    @Provides
    @Singleton
    fun provideAuthUserUseCase(
        getCurrentUserUseCase: GetCurrentUserUseCase,
        loginUseCase: LoginUseCase,
        signUpUseCase: SignUpUseCase
    ): AuthUserUseCase {
        return AuthUserUseCase(getCurrentUserUseCase, loginUseCase, signUpUseCase)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductsUseCase(productRepository: ProductRepository): GetProductsUseCase {
        return GetProductsUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductUseCase(productRepository: ProductRepository): GetProductUseCase {
        return GetProductUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideGetUsersUseCase(userRepository: UserRepository): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideFireStoreOperations(
        fireStore: FirebaseFirestore,
        firebaseStorage: StorageReference,
        firebaseAuth: FirebaseAuth
    ): FireStoreOperations {
        return FireStoreOperations(fireStore, firebaseStorage, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        fireStore: FirebaseFirestore,
        fireStoreOperations: FireStoreOperations
    ): UserRepository {
        return UserRepositoryImpl(fireStore, fireStoreOperations)
    }

    @Provides
    @Singleton
    fun provideUpdateWishlistRepository(fireStoreOperations: FireStoreOperations): UpdateWishlistRepository {
        return UpdateWishlistRepositoryImpl(fireStoreOperations)
    }

    @Provides
    @Singleton
    fun provideUpdateUserWishlistUseCase(updateWishlistRepository: UpdateWishlistRepository): UpdateUserWishlistUseCase {
        return UpdateUserWishlistUseCase(updateWishlistRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateCartRepository(fireStoreOperations: FireStoreOperations): UpdateCartRepository {
        return UpdateCartRepositoryImpl(fireStoreOperations)
    }

    @Provides
    @Singleton
    fun provideUpdateUserCartUseCase(updateCartRepository: UpdateCartRepository): UpdateUserCartUseCase {
        return UpdateUserCartUseCase(updateCartRepository)
    }

    @Provides
    @Singleton
    fun provideLogOutUserUseCase(authRepository: AuthRepository): LogoutUserUseCase {
        return LogoutUserUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateProfileUseCase(userRepository: UserRepository): UpdateProfileUseCase {
        return UpdateProfileUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateAddressUseCase(userRepository: UserRepository): UpdateAddressUseCase {
        return UpdateAddressUseCase(userRepository)
    }

}