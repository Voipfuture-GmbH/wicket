/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket;

/**
 * Holder for specifying Wicket page classes that have special meaning to an
 * application, such as an application's home page and any error display pages.
 * <p>
 * <i>homePage </i> (required, no default) - You must set this property to the
 * bookmarkable page that you want the framework to respond with when no path
 * information is specified.
 * <p>
 * <i>internalErrorPage </i>- You can override this with your own page class to
 * display internal errors in a different way.
 * <p>
 * <i>pageExpiredErrorPage </i>- You can override this with your own
 * bookmarkable page class to display expired page errors in a different way.
 * 
 * @author Jonathan Locke
 */
public class ApplicationPages
{
    /** Home page class */
    private Class homePageClass;

    /** Class of internal error page */
    private Class internalErrorPageClass;

    /** The error page displayed when an expired page is accessed */
    private Class pageExpiredErrorPageClass;

    /**
     * Gets home page class.
     * 
     * @return Returns the homePage.
     * @see ApplicationPages#setHomePage(Class)
     */
    public final Class getHomePage()
    {
        // If no home page is available
        if (homePageClass == null)
        {
            // give up with an exception
            throw new IllegalStateException("No home page was specified in application settings");
        }

        return homePageClass;
    }

    /**
     * Gets internal error page class.
     * 
     * @return Returns the internalErrorPage.
     * @see ApplicationPages#setInternalErrorPage(Class)
     */
    public final Class getInternalErrorPage()
    {
        return internalErrorPageClass;
    }

    /**
     * Gets the page expired page class.
     * 
     * @return Returns the pageExpiredErrorPage.
     * @see ApplicationPages#setPageExpiredErrorPage(Class)
     */
    public final Class getPageExpiredErrorPage()
    {
        return pageExpiredErrorPageClass;
    }

    /**
     * Sets home page class. The class must be external / bookmarkable and
     * therefore must extend Page.
     * 
     * @param homePage
     *            The home page class
     * @return This
     */
    public final ApplicationPages setHomePage(final Class homePage)
    {
        this.homePageClass = homePage;
        return this;
    }

    /**
     * Sets internal error page class. The class must be external / bookmarkable
     * and therefore must extend Page and must be able to construct from
     * PageParameters.
     * 
     * @param internalErrorPage
     *            The internalErrorPage to set.
     * @return This
     */
    public final ApplicationPages setInternalErrorPage(final Class internalErrorPage)
    {
        this.internalErrorPageClass = internalErrorPage;
        return this;
    }

    /**
     * Sets the page expired page class. The class must be external /
     * bookmarkable and therefore must extend Page and must be able to construct
     * from PageParameters.
     * 
     * @param pageExpiredErrorPage
     *            The pageExpiredErrorPage to set.
     * @return This
     */
    public final ApplicationPages setPageExpiredErrorPage(final Class pageExpiredErrorPage)
    {
        this.pageExpiredErrorPageClass = pageExpiredErrorPage;
        return this;
    }
}