package com.ejabi.yourcart.ui.Dialogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.ejabi.domain.model.Product
import com.ejabi.yourcart.adabter.ProductImageAdapter
import com.ejabi.yourcart.databinding.DialogProductDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * ProductDetailDialog is a BottomSheetDialogFragment.
 *
 * WHY BottomSheetDialogFragment instead of Dialog?
 * - Material Design standard for product detail overlays
 * - Supports swipe-to-dismiss naturally
 * - Better UX on phones (slides up from bottom, feels native)
 * - Can be expanded to full screen for more content
 *
 * Constructor parameters:
 * - product: the item to display
 * - onAddToCart: callback lambda → called when user taps "Add to Cart"
 *   Using a lambda instead of an interface keeps it concise
 *
 * NOTE: For fragment arguments, you'd normally use companion object + Bundle.
 * Here we use constructor params for simplicity since we create it directly in Activity.
 */
class ProductDetailDialog(
    private val product: Product,
    private val onAddToCart: (Product) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogProductDetailBinding? = null
    private val binding get() = _binding!!

    // Track quantity selected before adding to cart
    private var selectedQuantity = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Expand bottom sheet to full height by default
        val bottomSheet = dialog?.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        )
        bottomSheet?.let {
            BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
        }

        setupContent()
        setupImagePager()
        setupQuantityControls()
        setupAddToCart()
        animateIn()
    }

    private fun setupContent() {
        binding.apply {
            tvProductTitle.text = product.title
            tvProductCategory.text = product.category
            tvProductPrice.text = "$${product.price}"
            tvProductDescription.text = product.description

            // Rating with star display
            val rating = product.rating
            tvRatingValue.text = String.format("%.1f", rating)
            ratingBar.rating = rating.toFloat()
        }
    }

    /**
     * Sets up the ViewPager2 for product images with dots indicator.
     * ViewPager2 is the modern replacement for ViewPager — uses RecyclerView internally.
     *
     * TabLayoutMediator links the dots (TabLayout) to the ViewPager2:
     * - As you swipe images, the correct dot lights up automatically
     */
    private fun setupImagePager() {
        binding.viewPagerImages.adapter = ProductImageAdapter(product.images)

        // Connect dots indicator to ViewPager
        TabLayoutMediator(binding.dotsIndicator, binding.viewPagerImages) { _, _ ->
            // Tab text is empty — we just want dots
        }.attach()
    }

    /**
     * Quantity +/- controls.
     * Quantity is stored locally in this dialog (not yet in ViewModel).
     * When "Add to Cart" is tapped, the quantity goes with the product.
     *
     * Currently the ViewModel's addToCart() adds 1 each call.
     * For multi-quantity support, you'd pass quantity to CartItem.
     */
    private fun setupQuantityControls() {
        updateQuantityDisplay()

        binding.btnIncrease.setOnClickListener {
            selectedQuantity++
            updateQuantityDisplay()
            // Bounce animation on the count
            binding.tvQuantity.animate()
                .scaleX(1.3f).scaleY(1.3f).setDuration(100)
                .withEndAction {
                    binding.tvQuantity.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }.start()
        }

        binding.btnDecrease.setOnClickListener {
            if (selectedQuantity > 1) {
                selectedQuantity--
                updateQuantityDisplay()
            }
        }
    }

    private fun updateQuantityDisplay() {
        binding.tvQuantity.text = selectedQuantity.toString()
        binding.btnDecrease.isEnabled = selectedQuantity > 1
        binding.btnDecrease.alpha = if (selectedQuantity > 1) 1f else 0.4f
    }

    /**
     * "Add to Cart" button.
     * 1. Calls the lambda passed by MainActivity → ViewModel.addToCart()
     * 2. Shows a success animation
     * 3. Dismisses the dialog
     *
     * WHY a lambda callback instead of the ViewModel directly?
     * - The dialog doesn't need to know about ViewModel
     * - Pure separation: Dialog only knows about Product
     * - Makes the dialog reusable in any screen
     */
    private fun setupAddToCart() {
        binding.btnAddToCart.setOnClickListener {
            // Button bounce animation before dismissing
            it.animate()
                .scaleX(0.92f).scaleY(0.92f).setDuration(80)
                .withEndAction {
                    it.animate()
                        .scaleX(1f).scaleY(1f)
                        .setInterpolator(OvershootInterpolator(2f))
                        .setDuration(200)
                        .withEndAction {
                            // Repeat call for selectedQuantity times
                            // (or pass quantity to addItem — either works)
                            repeat(selectedQuantity) {
                                onAddToCart(product)
                            }
                            dismiss()
                        }.start()
                }.start()
        }
    }

    /**
     * Slide-in animation when dialog opens.
     * The content fades in and translates up slightly for polish.
     */
    private fun animateIn() {
        binding.root.translationY = 80f
        binding.root.alpha = 0f
        binding.root.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(350)
            .setInterpolator(OvershootInterpolator(0.8f))
            .start()
    }

    /**
     * Always null the binding in onDestroyView.
     * WHY? Fragments outlive their views. If you keep a View reference in a Fragment
     * after the view is destroyed, you get a memory leak.
     * Setting _binding = null allows GC to collect the old view.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}