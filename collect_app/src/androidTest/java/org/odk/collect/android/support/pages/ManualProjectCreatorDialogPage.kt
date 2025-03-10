package org.odk.collect.android.support.pages

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.odk.collect.android.R

class ManualProjectCreatorDialogPage : Page<ManualProjectCreatorDialogPage>() {
    override fun assertOnPage(): ManualProjectCreatorDialogPage {
        assertText(R.string.add_project)
        return this
    }

    fun inputUrl(url: String): ManualProjectCreatorDialogPage {
        inputText(R.string.server_url, url)
        return this
    }

    fun inputUsername(username: String): ManualProjectCreatorDialogPage {
        inputText(R.string.username, username)
        return this
    }

    fun inputPassword(password: String): ManualProjectCreatorDialogPage {
        inputText(R.string.password, password)
        return this
    }

    fun addProject(): MainMenuPage {
        Espresso.onView(withId(R.id.add_button)).perform(ViewActions.click())
        return MainMenuPage().assertOnPage()
    }
}
